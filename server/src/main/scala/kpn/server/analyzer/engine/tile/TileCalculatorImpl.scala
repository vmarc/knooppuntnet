package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileCache
import org.springframework.stereotype.Component

@Component
class TileCalculatorImpl extends TileCalculator {

  private val cache = new TileCache()

  def tileContainingWorldCoordinate(z: Int, worldX: Double, worldY: Double): Tile = {
    cache.tileContainingWorldCoordinate(z, worldX, worldY)
  }

  def tileXY(z: Int, x: Int, y: Int): Tile = {
    cache(z, x, y)
  }

}
