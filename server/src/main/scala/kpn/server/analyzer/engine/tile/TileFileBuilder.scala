package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.OldTileData
import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileFileBuilder {
  def build(tileData: OldTileData, tile: Tile): Unit
}
