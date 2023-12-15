package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import kpn.server.analyzer.engine.tiles.domain.OldTile

trait RouteTileCalculator {

  /*
    Determines all tiles that will be needed to display given route
    at given zoom level.
   */
  def tiles(z: Int, segments: Seq[RouteTileSegment]): Seq[OldTile]
}
