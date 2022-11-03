package kpn.server.analyzer.engine.monitor

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.NetworkType
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

object MonitorRouteAnalysisSupport {

  private val geomFactory = new GeometryFactory
  private val log = Log(classOf[MonitorRouteAnalysisSupport])

  def toRoute(groupId: ObjectId, routeName: String, description: String, relationId: Option[Long]): MonitorRoute = {
    MonitorRoute(
      ObjectId(),
      groupId,
      routeName,
      description,
      relationId
    )
  }

  def toRouteSegments(routeRelation: Relation): Seq[MonitorRouteSegmentData] = {

    val fragmentMap = log.infoElapsed {
      val allRelations = Seq(routeRelation) ++ routeRelation.relationMembers.map(_.relation)
      val allWayMembers = allRelations.flatMap(relation => relation.wayMembers)
      ("fragment analyzer", new FragmentAnalyzer(Seq.empty, allWayMembers).fragmentMap)
    }

    val segments = log.infoElapsed {
      ("segment builder", new SegmentBuilder(NetworkType.hiking, fragmentMap, pavedUnpavedSplittingEnabled = false).segments(fragmentMap.ids))
    }

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

class MonitorRouteAnalysisSupport {
}
