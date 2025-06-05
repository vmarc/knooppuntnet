package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileCalculator {

  def tileNamed(tileName: String): Tile
}
