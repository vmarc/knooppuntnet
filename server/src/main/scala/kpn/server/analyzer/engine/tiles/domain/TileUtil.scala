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

  def tileCoordinates(tileContext: TileContext, tile: Tile, worldCoordinates: Seq[Coordinate]): Seq[TileCoordinate] = {
    val scaledCoordinates = tileContext.toTileCoordinate(tile, worldCoordinates)
    val lineString = geometryFactory.createLineString(scaledCoordinates.toArray)
    val simplifiedLineString = if (!tileContext.detailed) {
      DouglasPeuckerSimplifier.simplify(lineString, SimplificationTolerance).asInstanceOf[LineString]
    }
    else {
      lineString
    }

    if (simplifiedLineString.getLength < MinimumGeometryLength) {
      Seq.empty
    }
    else {
      val clippedGeometry = clipGeometry(tileContext, simplifiedLineString)

      // ignore geometry if empty after clipping
      if (clippedGeometry.isEmpty) {
        Seq.empty
      }
      else {
        if (clippedGeometry.getLength < MinimumGeometryLength) {
          Seq.empty
        }
        else {
          clippedGeometry.getCoordinates.toSeq.map(coordinate => TileCoordinate(Math.floor(coordinate.x).toInt, Math.floor(coordinate.y).toInt))
        }
      }
    }
  }

  private def clipGeometry(tileContext: TileContext, geometry: Geometry): Geometry = {
    try {
      var clippedGeometry = tileContext.tileEnvelope.intersection(geometry)
      // some times a intersection is returned as an empty geometry.
      // going via wkt fixes the problem.
      if (clippedGeometry.isEmpty && geometry.intersects(tileContext.tileEnvelope)) {
        val originalViaWkt = new WKTReader().read(geometry.toText)
        clippedGeometry = tileContext.tileEnvelope.intersection(originalViaWkt)
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
