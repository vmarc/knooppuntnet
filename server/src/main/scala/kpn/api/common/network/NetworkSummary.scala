package kpn.api.common.network

import kpn.api.custom.NetworkType

case class NetworkSummary(
  networkType: NetworkType,
  name: String,
  factCount: Int,
  nodeCount: Int,
  routeCount: Int,
  changeCount: Int,
  active: Boolean
)
