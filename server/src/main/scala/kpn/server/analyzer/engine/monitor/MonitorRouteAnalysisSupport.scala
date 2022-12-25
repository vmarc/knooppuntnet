package kpn.server.analyzer.engine.monitor

import kpn.api.common.Bounds
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.core.common.RelationUtil
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonWriter

object MonitorRouteAnalysisSupport {

  private val geomFactory = new GeometryFactory

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

  def toMultiLineString(sampleCoordinates: Seq[Coordinate], sequences: Seq[ReferenceCoordinateSequence]): MultiLineString = {
    geomFactory.createMultiLineString(sequences.map(sequence => toLineString(sampleCoordinates, sequence)).toArray)
  }

  def toLineString(osmCoordinates: Seq[Coordinate], sequence: ReferenceCoordinateSequence): LineString = {
    val coordinates = if (sequence.indexes.size == 1) {
      Seq(osmCoordinates.head, osmCoordinates.head) // TODO investigate why this is useful
    }
    else {
      sequence.indexes.map(index => osmCoordinates(index))
    }
    geomFactory.createLineString(coordinates.toArray)
  }

  def toSampleCoordinates(sampleDistanceMeters: Int, lineString: LineString): Seq[Coordinate] = {
    val referenceMeters = toMeters(lineString.getLength)
    val distanceBetweenSamples = sampleDistanceMeters.toDouble * lineString.getLength / referenceMeters
    val densifiedLineString = Densifier.densify(lineString, distanceBetweenSamples)
    densifiedLineString.getCoordinates.toSeq
  }

  def filteredWayMembers(relation: Relation): Seq[WayMember] = {
    val allRelations = RelationUtil.relationsInRelation(relation)
    val allWayMembers = allRelations.flatMap(relation => relation.wayMembers)
    MonitorRouteFilter.filterWayMembers(allWayMembers)
  }

}
