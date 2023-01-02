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

import scala.annotation.tailrec

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

  def geometryBounds(geometry: Geometry): Bounds = {
    val envelope = geometry.getEnvelopeInternal
    Bounds(
      envelope.getMinY, // minLat
      envelope.getMinX, // minLon
      envelope.getMaxY, // maxLat
      envelope.getMaxX, // maxLon
    )
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
    val lineStrings = sequences.map(sequence => toLineString(sampleCoordinates, sequence))
    geomFactory.createMultiLineString(lineStrings.toArray)
  }

  def toLineString(osmCoordinates: Seq[Coordinate], sequence: ReferenceCoordinateSequence): LineString = {
    val coordinates = if (sequence.indexes.size == 1) {
      Seq(osmCoordinates.head, osmCoordinates.head) // TODO investigate why this is useful
    }
    else {
      simplifyCoordinates(sequence.indexes.map(index => osmCoordinates(index)).toList)
    }
    geomFactory.createLineString(coordinates.toArray)
  }

  private def simplifyCoordinates(coordinates: List[Coordinate]): List[Coordinate] = {
    if (coordinates.size < 3) {
      coordinates
    }
    else {
      simplify2(List(coordinates.head), coordinates.head, coordinates.tail).reverse
    }
  }

  @tailrec
  private def simplify2(selectedCoordinates: List[Coordinate], currentCoordinate: Coordinate, remainder: List[Coordinate]): List[Coordinate] = {
    remainder.headOption match {
      case None => selectedCoordinates
      case Some(middleCoordinate) =>
        val newRemainder = remainder.tail
        newRemainder.headOption match {
          case None => middleCoordinate :: selectedCoordinates
          case Some(nextCoordinate) =>
            val lineString = geomFactory.createLineString(Array(currentCoordinate, nextCoordinate))
            val point = geomFactory.createPoint(middleCoordinate)
            val distance = toMeters(lineString.distance(point))
            if (distance < 0.5d) {
              // swallow the  middle coordinate
              simplify2(selectedCoordinates, currentCoordinate, newRemainder)
            }
            else {
              // keep the  middle coordinate, and continue simplifying
              simplify2(middleCoordinate :: selectedCoordinates, middleCoordinate, newRemainder)
            }
        }
    }
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
    MonitorFilter.filterWayMembers(allWayMembers)
  }

}
