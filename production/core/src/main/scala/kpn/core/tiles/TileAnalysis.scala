package kpn.core.tiles

import kpn.shared.NetworkType
import kpn.shared.NodeInfo
import kpn.shared.network.NetworkNodeInfo2
import kpn.shared.route.RouteInfo

case class TileAnalysis(
  networkType: NetworkType,
  nodes: Seq[NetworkNodeInfo2],
  orphanNodes: Seq[NodeInfo],
  routeInfos: Seq[RouteInfo]
)
