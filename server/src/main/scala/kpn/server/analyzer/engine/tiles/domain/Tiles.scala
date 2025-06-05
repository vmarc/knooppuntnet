package kpn.server.analyzer.engine.tiles.domain

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Polygon

abstract class Tiles(tileContexts: Map[Int, TileContext]) {

  def toTileCoordinate(tile: Tile, worldCoordinates: Seq[Coordinate]): Seq[Coordinate] = {
    worldCoordinates.map(c => toTileCoordinate(tile, c))
  }

  def toTileCoordinate(tile: Tile, worldCoordinate: Coordinate): Coordinate = {
    val tileContext = tileContexts(tile.z)
    tileContext.toTileCoordinate(tile, worldCoordinate)
  }

  def tile(tileId: TileId): Tile = {
    val tileContext = tileContexts(tileId.z)
    tileContext.tile(tileId)
  }

  def tileEnvelope(zoomLevel: Int): Polygon = {
    val tileContext = tileContexts(zoomLevel)
    tileContext.tileEnvelope
  }

  def detailed(zoomLevel: Int): Boolean = {
    val tileContext = tileContexts(zoomLevel)
    tileContext.detailed
  }

  def extent(zoomLevel: Int): Int = {
    val tileContext = tileContexts(zoomLevel)
    tileContext.extent
  }

  def clipBufferSize(zoomLevel: Int): Int = {
    val tileContext = tileContexts(zoomLevel)
    tileContext.clipBufferSize
  }
}

