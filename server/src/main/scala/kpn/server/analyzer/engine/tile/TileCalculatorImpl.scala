package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.RouteTileCache
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.springframework.stereotype.Component

@Component
class TileCalculatorImpl extends TileCalculator {

  private val cache = new RouteTileCache()

  def tileNamed(tileName: String): Tile = {
    cache(tileName)
  }
}
