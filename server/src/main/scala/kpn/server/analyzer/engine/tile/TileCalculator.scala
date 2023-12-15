package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile

trait TileCalculator {

  def tileContainingWorldCoordinate(z: Int, worldX: Double, worldY: Double): Tile

  def tileXY(z: Int, x: Int, y: Int): Tile

}
