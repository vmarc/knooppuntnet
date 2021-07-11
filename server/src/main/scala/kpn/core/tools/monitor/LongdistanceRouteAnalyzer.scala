package kpn.core.tools.monitor

import kpn.api.common.Bounds
import kpn.api.common.monitor.LongdistanceRoute
import kpn.api.common.monitor.LongdistanceRouteNokSegment
import kpn.api.common.monitor.LongdistanceRouteSegment
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.FragmentMap
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonWriter

object LongdistanceRouteAnalyzer {

  private val geomFactory = new GeometryFactory
  private val sampleDistanceMeters = 10
  private val toleranceMeters = 10

  def analyze(gpxFilename: String, gpxLineString: LineString, routeRelation: Relation, osmRouteSegments: Seq[LongdistanceRouteSegmentData]): LongdistanceRoute = {

    val (okOption: Option[MultiLineString], nokSegments: Seq[LongdistanceRouteNokSegment]) = {

      val distanceBetweenSamples = sampleDistanceMeters.toDouble * gpxLineString.getLength / toMeters(gpxLineString.getLength)
      val densifiedGpx = Densifier.densify(gpxLineString, distanceBetweenSamples)
      val sampleCoordinates = densifiedGpx.getCoordinates.toSeq

      val distances = sampleCoordinates.toList.map { coordinate =>
        val point = geomFactory.createPoint(coordinate)
        toMeters(osmRouteSegments.map(segment => segment.lineString.distance(point)).min)
      }

      val withinTolerance = distances.map(distance => distance < toleranceMeters)
      val okAndIndexes = withinTolerance.zipWithIndex.map { case (ok, index) => ok -> index }
      val splittedOkAndIndexes = split(okAndIndexes)

      val ok: MultiLineString = toMultiLineString(sampleCoordinates, splittedOkAndIndexes.filter(_.head._1))

      val noks = splittedOkAndIndexes.filterNot(_.head._1)

      val nok = noks.zipWithIndex.map { case (segment, segmentIndex) =>
        val segmentIndexes = segment.map(_._2)
        val maxDistance = distances.zipWithIndex.filter { case (distance, index) =>
          segmentIndexes.contains(index)
        }.map { case (distance, index) =>
          distance
        }.max

        val lineString = toLineString(sampleCoordinates, segment)
        val meters: Long = Math.round(toMeters(lineString.getLength))
        val bounds = toBounds(lineString.getCoordinates.toSeq).toBoundsI
        val geoJson = toGeoJson(lineString)

        LongdistanceRouteNokSegment(
          segmentIndex + 1,
          meters,
          maxDistance.toLong,
          bounds,
          geoJson
        )
      }

      val xx: Seq[LongdistanceRouteNokSegment] = nok.sortBy(_.distance).reverse.zipWithIndex.map { case (s, index) =>
        s.copy(id = index + 1)
      }

      (Some(ok), xx)
    }

    val gpxDistance = Math.round(toMeters(gpxLineString.getLength / 1000))
    val osmDistance = Math.round(osmRouteSegments.map(_.segment.meters).sum.toDouble / 1000)

    val gpxGeometry = toGeoJson(gpxLineString)
    val okGeometry = okOption.map(geometry => toGeoJson(geometry))

    // TODO merge gpx bounds + ok
    val bounds = Util.mergeBounds(osmRouteSegments.map(_.segment.bounds) ++ nokSegments.map(_.bounds))

    LongdistanceRoute(
      routeRelation.id,
      routeRelation.tags("ref"),
      routeRelation.tags("name").getOrElse(s"$routeRelation.id"),
      routeRelation.tags("name:nl"),
      routeRelation.tags("name:en"),
      routeRelation.tags("name:de"),
      routeRelation.tags("name:fr"),
      None,
      routeRelation.tags("operator"),
      routeRelation.tags("website"),
      routeRelation.wayMembers.size,
      osmDistance,
      gpxDistance,
      bounds,
      Some(gpxFilename),
      osmRouteSegments.map(_.segment),
      Some(gpxGeometry),
      okGeometry,
      nokSegments
    )

  }


  def toRouteSegments(routeRelation: Relation): Seq[LongdistanceRouteSegmentData] = {

    val fragmentMap = withoutTags(new FragmentAnalyzer(Seq.empty, routeRelation.wayMembers).fragmentMap)
    val segments = new SegmentBuilder(fragmentMap).segments(fragmentMap.ids)

    //    println(s"wayMembers.size=${routeRelation.wayMembers.size}")
    //    println(s"fragments.size=${fragments.size}")
    //    println(s"segments.size=${segments.size}")

    segments.zipWithIndex.map { case (segment, index) =>

      val lineString = geomFactory.createLineString(segment.nodes.map(node => new Coordinate(node.lon, node.lat)).toArray)
      val meters: Long = Math.round(toMeters(lineString.getLength))
      val bounds = toBounds(lineString.getCoordinates.toSeq).toBoundsI
      val geoJson = toGeoJson(lineString)

      LongdistanceRouteSegmentData(
        LongdistanceRouteSegment(
          index + 1,
          meters,
          bounds,
          geoJson
        ),
        lineString
      )
    }
  }

  def toBounds(coordinates: Seq[Coordinate]): Bounds = {
    val minLat = coordinates.map(_.getY).min
    val maxLat = coordinates.map(_.getY).max
    val minLon = coordinates.map(_.getX).min
    val maxLon = coordinates.map(_.getX).max
    new Bounds(
      minLat,
      minLon,
      maxLat,
      maxLon
    )
  }

  def toMeters(value: Double): Double = {
    value * (math.Pi / 180) * 6378137
  }

  def toGeoJson(geometry: Geometry): String = {
    new GeoJsonWriter().write(geometry).replaceAll("EPSG:0", "EPSG:4326")
  }

  private def split(list: List[(Boolean, Int)]): List[List[(Boolean, Int)]] = {
    list match {
      case Nil => Nil
      case head :: tail =>
        val segment = list.takeWhile(_._1 == head._1)
        segment +: split(list.drop(segment.length))
    }
  }

  private def toMultiLineString(sampleCoordinates: Seq[Coordinate], segments: List[List[(Boolean, Int)]]) = {
    geomFactory.createMultiLineString(segments.map(segment => toLineString(sampleCoordinates, segment)).toArray)
  }

  private def toLineString(osmCoordinates: Seq[Coordinate], segment: List[(Boolean, Int)]): LineString = {
    val indexes = segment.map(_._2)
    val coordinates = if (indexes.size == 1) {
      Seq(osmCoordinates.head, osmCoordinates.head)
    }
    else {
      indexes.map(index => osmCoordinates(index))
    }
    geomFactory.createLineString(coordinates.toArray)
  }

  private def withoutTags(fragments: FragmentMap): FragmentMap = {
    new FragmentMap(
      fragments.all.map { fragment => // temporary hack to remove paved/unpaved info
        val rawWayCopy = fragment.way.raw.copy(tags = Tags.empty)
        val wayCopy = fragment.way.copy(raw = rawWayCopy)
        fragment.copy(way = wayCopy)
      }
    )
  }

}
