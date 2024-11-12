package kpn.server.analyzer.engine.tiles

import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileBuilder {
  def build(data: TileData, tile: Tile): Array[Byte]
}
