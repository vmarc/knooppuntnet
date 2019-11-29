package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.core.tiles.domain.Tile

trait NodeTileAnalyzer {

  def tiles(z: Int, latLon: LatLon): Seq[Tile]
}
