package kpn.api.common.network

import kpn.api.common.TimeInfo
import kpn.api.custom.NetworkType

case class NetworkNodesPage(
  timeInfo: TimeInfo,
  networkSummary: NetworkSummary,
  networkType: NetworkType,
  nodes: Seq[NetworkInfoNode],
  routeIds: Seq[Long]
)
