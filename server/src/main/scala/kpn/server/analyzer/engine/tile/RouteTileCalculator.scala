package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.OldTile
import kpn.server.analyzer.engine.tiles.domain.TileDataRouteSegment

trait RouteTileCalculator {

  /*
    Determines all tiles that will be needed to display given route
    at given zoom level.
   */
  def tiles(z: Int, segments: Seq[TileDataRouteSegment]): Seq[OldTile]
}
