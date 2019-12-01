package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.TileData

trait TileFileBuilder {
  def build(tileData: TileData): Unit
}
