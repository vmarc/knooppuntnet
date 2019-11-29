package kpn.server.analyzer.engine.tile

import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileCache
import org.springframework.stereotype.Component

@Component
class TileCalculatorImpl extends TileCalculator {

  private val cache = new TileCache()

  def get(z: Int, lon: Double, lat: Double): Tile = {
    cache(z, lon, lat)
  }

  def get(z: Int, x: Int, y: Int): Tile = {
    cache(z, x, y)
  }

  def get(tileName: String): Tile = {
    cache(tileName)
  }

}
