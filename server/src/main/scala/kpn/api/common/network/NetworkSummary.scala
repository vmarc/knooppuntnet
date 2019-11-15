package kpn.api.common.network

import kpn.api.custom.NetworkType

case class NetworkSummary(
  networkType: NetworkType,
  name: String,
  factCount: Long,
  nodeCount: Long,
  routeCount: Long,
  changeCount: Long,
  active: Boolean
)
