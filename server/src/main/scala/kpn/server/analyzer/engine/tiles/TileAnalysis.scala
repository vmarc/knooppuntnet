package kpn.server.analyzer.engine.tiles

import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

case class TileAnalysis(
  networkType: NetworkType,
  nodes: Seq[TileDataNode],
  routes: Seq[RouteTileInfo]
)
