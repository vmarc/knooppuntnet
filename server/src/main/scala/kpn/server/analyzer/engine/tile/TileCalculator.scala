package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileCalculator {

  def get(z: Int, lon: Double, lat: Double): Tile

  def get(z: Int, x: Int, y: Int): Tile

  def get(tileName: String): Tile

}
