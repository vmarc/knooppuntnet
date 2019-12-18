package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileCalculator {

  def tileLonLat(z: Int, lon: Double, lat: Double): Tile

  def tileXY(z: Int, x: Int, y: Int): Tile

  def tileNamed(tileName: String): Tile

}
