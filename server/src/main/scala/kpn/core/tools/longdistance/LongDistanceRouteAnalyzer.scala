package kpn.core.tools.longdistance

import kpn.api.common.Bounds
import kpn.api.common.longdistance.LongDistanceRouteSegment
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonWriter

object LongDistanceRouteAnalyzer {

  private val geomFactory = new GeometryFactory

  def toRouteSegments(routeRelation: Relation): Seq[LongDistanceRouteSegmentData] = {

    val fragments: Seq[Fragment] = new FragmentAnalyzer(Seq(), routeRelation.wayMembers).fragments


    val fragmentsCopy = fragments.map { fragment => // temporary hack to remove paved/unpaved info
      val rawWayCopy = fragment.way.raw.copy(tags = Tags.empty)
      val wayCopy = fragment.way.copy(raw = rawWayCopy)
      fragment.copy(way = wayCopy)
    }
    val fragmentMap = fragmentsCopy.map(f => f.id -> f).toMap
    val fragmentIds = fragmentMap.values.map(_.id).toSet
    val segments = new SegmentBuilder(fragmentMap).segments(fragmentIds)

    //    println(s"wayMembers.size=${routeRelation.wayMembers.size}")
    //    println(s"fragments.size=${fragments.size}")
    //    println(s"segments.size=${segments.size}")

    segments.zipWithIndex.map { case (segment, index) =>

      val lineString = geomFactory.createLineString(segment.nodes.map(node => new Coordinate(node.lon, node.lat)).toArray)
      val meters: Long = Math.round(toMeters(lineString.getLength))
      val bounds = toBounds(lineString.getCoordinates.toSeq).toBoundsI
      val geoJson = toGeoJson(lineString)

      LongDistanceRouteSegmentData(
        LongDistanceRouteSegment(
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

}
