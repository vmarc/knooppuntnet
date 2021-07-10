package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute

trait RouteTileCalculator {

  /*
    Determines all tiles that will be needed to display given route
    at given zoom level.
   */
  def tiles(z: Int, tileRoute: TileDataRoute): Seq[Tile]
}
