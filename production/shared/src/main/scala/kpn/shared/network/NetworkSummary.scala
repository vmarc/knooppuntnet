package kpn.shared.network

import kpn.shared.NetworkType

case class NetworkSummary(
  networkType: NetworkType,
  name: String,
  factCount: Int,
  nodeCount: Int,
  routeCount: Int,
  changeCount: Int,
  active: Boolean
)
