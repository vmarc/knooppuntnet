package kpn.api.common.network

import kpn.api.common.NetworkType
import kpn.api.custom.NetworkScope

case class NetworkSummary(
  name: String,
  networkType: NetworkType,
  networkScope: NetworkScope,
  factCount: Long,
  nodeCount: Long,
  routeCount: Long,
  changeCount: Long
)
