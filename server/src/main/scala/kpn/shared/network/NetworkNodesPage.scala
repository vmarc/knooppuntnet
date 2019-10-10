package kpn.shared.network

import kpn.shared.NetworkType
import kpn.shared.TimeInfo

case class NetworkNodesPage(
  timeInfo: TimeInfo,
  networkSummary: NetworkSummary,
  networkType: NetworkType,
  nodes: Seq[NetworkNodeInfo2],
  routeIds: Seq[Long]
)
