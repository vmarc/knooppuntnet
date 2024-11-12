package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileFileBuilder {
  def build(tileData: TileData, tile: Tile): Unit
}
