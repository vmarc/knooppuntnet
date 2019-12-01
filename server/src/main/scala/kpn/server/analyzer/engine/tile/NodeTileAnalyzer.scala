package kpn.server.analyzer.engine.tile

import kpn.api.common.LatLon
import kpn.server.analyzer.engine.tiles.domain.Tile

trait NodeTileAnalyzer {

  def tiles(z: Int, latLon: LatLon): Seq[Tile]
}
