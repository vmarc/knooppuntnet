package kpn.server.analyzer.engine.tile

import kpn.core.tiles.TileData

trait TileFileBuilder {
  def build(tileData: TileData): Unit
}
