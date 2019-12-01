package kpn.server.analyzer.engine.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.route.RouteInfo
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.tiles.domain.TileDataNode

case class TileAnalysis(
  networkType: NetworkType,
  nodes: Seq[TileDataNode],
  orphanNodes: Seq[NodeInfo],
  routeInfos: Seq[RouteInfo]
)
