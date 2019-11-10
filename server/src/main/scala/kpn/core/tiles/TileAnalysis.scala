package kpn.core.tiles

import kpn.api.common.NodeInfo
import kpn.api.common.network.NetworkNodeInfo2
import kpn.api.common.route.RouteInfo
import kpn.api.custom.NetworkType

case class TileAnalysis(
  networkType: NetworkType,
  nodes: Seq[NetworkNodeInfo2],
  orphanNodes: Seq[NodeInfo],
  routeInfos: Seq[RouteInfo]
)
