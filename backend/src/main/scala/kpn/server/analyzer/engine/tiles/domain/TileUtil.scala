package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.TopologyException
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.WKTReader
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier

object TileUtil {
  private val geometryFactory = new GeometryFactory
  private val MinimumGeometryLength = 1.0d
  private val SimplificationTolerance = 1.0d

  def routeTileCoordinates(tile: Tile, worldCoordinates: Seq[Coordinate]): Seq[TileCoordinate] = {
    if (!tile.contains(worldCoordinates)) {
      return Seq.empty
    }
    val scaledCoordinates = RouteTiles.toTileCoordinate(tile, worldCoordinates)
    val lineString = geometryFactory.createLineString(scaledCoordinates.toArray)
    val simplifiedLineString = if (RouteTiles.detailed(tile.z)) {
      lineString
    }
    else {
      simplify(lineString)
    }

    if (simplifiedLineString.getLength < MinimumGeometryLength) {
      Seq.empty
    }
    else {
      val clippedGeometry = clipGeometry(tile, simplifiedLineString)

      // ignore geometry if empty after clipping
      if (clippedGeometry.isEmpty) {
        Seq.empty
      }
      else {
        if (clippedGeometry.getLength < MinimumGeometryLength) {
          Seq.empty
        }
        else {
          clippedGeometry.getCoordinates.toSeq.map(coordinate => TileCoordinate(Math.round(coordinate.x).toInt, Math.round(coordinate.y).toInt))
        }
      }
    }
  }

  private def simplify(lineString: LineString): LineString = {
    DouglasPeuckerSimplifier.simplify(lineString, SimplificationTolerance).asInstanceOf[LineString]
  }

  private def clipGeometry(tile: Tile, geometry: Geometry): Geometry = {
    try {
      val tileEnvelope = RouteTiles.tileEnvelope(tile.z)
      var clippedGeometry = tileEnvelope.intersection(geometry)
      // some times a intersection is returned as an empty geometry.
      // going via wkt fixes the problem.
      if (clippedGeometry.isEmpty && geometry.intersects(tileEnvelope)) {
        val originalViaWkt = new WKTReader().read(geometry.toText)
        clippedGeometry = tileEnvelope.intersection(originalViaWkt)
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

  def toTileLine(tile: Tile, worldCoordinates: Seq[Coordinate]): Option[String] = {
    val tileCoordinates = TileUtil.routeTileCoordinates(tile, worldCoordinates)
    if (tileCoordinates.nonEmpty) {
      Some(tileCoordinates
        .map(coordinate => s"[${coordinate.x},${coordinate.y}]")
        .mkString("[", ",", "]")
      )
    }
    else {
      None
    }
  }
}

