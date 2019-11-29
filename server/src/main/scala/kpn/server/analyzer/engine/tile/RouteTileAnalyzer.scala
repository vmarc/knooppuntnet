package kpn.server.analyzer.engine.tile

import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileDataRoute

trait RouteTileAnalyzer {

  /*
    Determines all tiles that will be needed to display given route
    at given zoom level.
   */
  def tiles(z: Int, tileRoute: TileDataRoute): Seq[Tile]
}
