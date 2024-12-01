package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineSegment
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.TopologyException
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.WKTReader
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier

object TileUtil {
  private val geometryFactory = new GeometryFactory

  def top(tile: Tile): LineSegment = {
    new LineSegment(
      tile.worldXMin,
      tile.worldYMin,
      tile.worldXMax,
      tile.worldYMin
    )
  }

  def bottom(tile: Tile): LineSegment = {
    new LineSegment(
      tile.worldXMin,
      tile.worldYMax,
      tile.worldXMax,
      tile.worldYMax
    )
  }

  def left(tile: Tile): LineSegment = {
    new LineSegment(
      tile.worldXMin,
      tile.worldYMin,
      tile.worldXMin,
      tile.worldYMax
    )
  }

  def right(tile: Tile): LineSegment = {
    new LineSegment(
      tile.worldXMax,
      tile.worldYMin,
      tile.worldXMax,
      tile.worldYMin
    )
  }

  def tileCoordinates(tile: Tile, worldCoordinates: Seq[Coordinate]): Seq[TileCoordinate] = {
    val scaledCoordinates = worldCoordinates.map(tile.scale)
    val lineString = geometryFactory.createLineString(scaledCoordinates.toArray)
    val simplifiedLineString = if (!tile.detailed) {
      DouglasPeuckerSimplifier.simplify(lineString, 1).asInstanceOf[LineString]
    }
    else {
      lineString
    }

    if (simplifiedLineString.getLength < 1.0d) {
      Seq.empty
    }
    else {
      val clippedGeometry = clipGeometry(tile, simplifiedLineString)

      // ignore geometry if empty after clipping
      if (clippedGeometry.isEmpty) {
        Seq.empty
      }
      else {
        if (clippedGeometry.getLength < 1.0d) {
          Seq.empty
        }
        else {
          clippedGeometry.getCoordinates().map(coordinate => TileCoordinate(Math.floor(coordinate.x).toInt, Math.floor(coordinate.y).toInt))
        }
      }
    }
  }

  private def clipGeometry(tile: Tile, geometry: Geometry): Geometry = {
    try {
      var clippedGeometry = tile.tileEnvelope.intersection(geometry)
      // some times a intersection is returned as an empty geometry.
      // going via wkt fixes the problem.
      if (clippedGeometry.isEmpty && geometry.intersects(tile.tileEnvelope)) {
        val originalViaWkt = new WKTReader().read(geometry.toText)
        clippedGeometry = tile.tileEnvelope.intersection(originalViaWkt)
      }
      clippedGeometry
    } catch {
      case e: TopologyException =>
        // could not intersect. original geometry will be used instead
        geometry
      case e1: ParseException =>
        // could not encode/decode WKT. original geometry will be used instead
        geometry
    }
  }
}
