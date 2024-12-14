package kpn.api.common.network

import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType

case class NetworkSummary(
  name: String,
  networkType: NetworkType,
  networkScope: NetworkScope,
  factCount: Long,
  nodeCount: Long,
  routeCount: Long,
  changeCount: Long
)
