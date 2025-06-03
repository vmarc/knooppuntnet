package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon

object TileContext {

  private val TILE_SIZE: Int = 256
  private val EXTENT_STANDARD: Int = TILE_SIZE
  private val EXTENT_DETAILED: Int = 4096

  def apply(detailed: Boolean, clipBufferSize: Int): TileContext = {

    val extent = if (detailed) {
      EXTENT_DETAILED
    }
    else {
      EXTENT_STANDARD
    }

    val tileEnvelope = calculateTileEnvelope(extent, clipBufferSize)

    TileContext(
      detailed,
      extent,
      clipBufferSize,
      tileEnvelope
    )
  }

  private def calculateTileEnvelope(extent: Int, clipBufferSize: Int): Polygon = {
    val size = extent.toDouble
    val coords = new Array[Coordinate](5)
    coords(0) = new Coordinate(0d - clipBufferSize, size + clipBufferSize)
    coords(1) = new Coordinate(size + clipBufferSize, size + clipBufferSize)
    coords(2) = new Coordinate(size + clipBufferSize, 0d - clipBufferSize)
    coords(3) = new Coordinate(0d - clipBufferSize, 0d - clipBufferSize)
    coords(4) = coords(0)
    new GeometryFactory().createPolygon(coords)
  }
}

case class TileContext(
  detailed: Boolean,
  extent: Int,
  clipBufferSize: Int,
  tileEnvelope: Polygon,
) {

  def scale(tile: Tile, worldCoordinates: Seq[Coordinate]): Seq[Coordinate] = {
    worldCoordinates.map(scale)
  }

  def scale(tile: Tile, worldCoordinate: Coordinate): Coordinate = {
    val scaledX = (worldCoordinate.x - tile.bounds.xMin) * extent / tile.bounds.width
    val scaledY = (worldCoordinate.y - tile.bounds.yMin) * extent / tile.bounds.height
    new Coordinate(scaledX, scaledY)
  }
}
