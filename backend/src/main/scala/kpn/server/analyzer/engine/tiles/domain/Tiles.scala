package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Polygon

abstract class Tiles(tileContexts: Map[Int, TileContext]) {

  def toTileCoordinate(tile: Tile, worldCoordinates: Seq[Coordinate]): Seq[Coordinate] = {
    worldCoordinates.map(c => toTileCoordinate(tile, c))
  }

  def toTileCoordinate(tile: Tile, worldCoordinate: Coordinate): Coordinate = {
    tileContext(tile.id.z).toTileCoordinate(tile, worldCoordinate)
  }

  def tile(tileId: TileId): Tile = {
    tileContext(tileId.z).tile(tileId)
  }

  def tileEnvelope(zoomLevel: Int): Polygon = {
    tileContext(zoomLevel).tileEnvelope
  }

  def detailed(zoomLevel: Int): Boolean = {
    tileContext(zoomLevel).detailed
  }

  def extent(zoomLevel: Int): Int = {
    tileContext(zoomLevel).extent
  }

  def clipBufferSize(zoomLevel: Int): Int = {
    tileContext(zoomLevel).clipBufferSize
  }

  private def tileContext(zoomLevel: Int): TileContext = {
    tileContexts.getOrElse(
      zoomLevel,
      {
        throw new IllegalArgumentException(s"Unknown zoom level: $zoomLevel")
      }
    )
  }
}
