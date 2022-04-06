package kpn.server.analyzer.engine.monitor

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.FragmentMap
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder
import kpn.server.api.monitor.domain.MonitorRoute
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonWriter

object MonitorRouteAnalyzer {

  private val geomFactory = new GeometryFactory
  private val log = Log(classOf[MonitorRouteAnalyzer])

  def toRoute(routeName: String, groupName: String, description: String, routeId: Long): MonitorRoute = {
    MonitorRoute(
      groupName + ":" + routeName,
      groupName,
      routeName,
      description,
      routeId
    )
  }

  def toRouteSegments(routeRelation: Relation): Seq[MonitorRouteSegmentData] = {

    val originalFragmentMap = log.infoElapsed {
      ("fragment analyzer", new FragmentAnalyzer(Seq.empty, routeRelation.wayMembers).fragmentMap)
    }

    val fragmentMap = withoutTags(originalFragmentMap)

    val segments = log.infoElapsed {
      ("segment builder", new SegmentBuilder(fragmentMap).segments(fragmentMap.ids))
    }
    //    println(s"wayMembers.size=${routeRelation.wayMembers.size}")
    //    println(s"fragments.size=${fragments.size}")
    //    println(s"segments.size=${segments.size}")

    segments.zipWithIndex.map { case (segment, index) =>

      val lineString = geomFactory.createLineString(segment.nodes.map(node => new Coordinate(node.lon, node.lat)).toArray)
      val meters: Long = Math.round(toMeters(lineString.getLength))
      val bounds = toBounds(lineString.getCoordinates.toSeq)
      val geoJson = toGeoJson(lineString)

      MonitorRouteSegmentData(
        MonitorRouteSegment(
          index + 1,
          meters,
          bounds,
          geoJson
        ),
        lineString
      )
    }
  }

  private def withoutTags(fragmentMap: FragmentMap): FragmentMap = {
    new FragmentMap(
      fragmentMap.all.map { fragment => // temporary hack to remove paved/unpaved info
        val rawWayCopy = fragment.way.raw.copy(tags = Tags.empty)
        val wayCopy = fragment.way.copy(raw = rawWayCopy)
        fragment.copy(way = wayCopy)
      }
    )
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

  def split(list: List[(Boolean, Int)]): List[List[(Boolean, Int)]] = {
    list match {
      case Nil => Nil
      case head :: tail =>
        val segment = list.takeWhile(_._1 == head._1)
        segment +: split(list.drop(segment.length))
    }
  }

  def toMultiLineString(sampleCoordinates: Seq[Coordinate], segments: List[List[(Boolean, Int)]]): MultiLineString = {
    geomFactory.createMultiLineString(segments.map(segment => toLineString(sampleCoordinates, segment)).toArray)
  }

  def toLineString(osmCoordinates: Seq[Coordinate], segment: List[(Boolean, Int)]): LineString = {
    val indexes = segment.map(_._2)
    val coordinates = if (indexes.size == 1) {
      Seq(osmCoordinates.head, osmCoordinates.head)
    }
    else {
      indexes.map(index => osmCoordinates(index))
    }
    geomFactory.createLineString(coordinates.toArray)
  }
}

class MonitorRouteAnalyzer {
}
