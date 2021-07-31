package kpn.api.common.network

import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType

case class NetworkSummary(
  name: String,
  networkType: NetworkType,
  networkScope: NetworkScope,
  factCount: Long,
  nodeCount: Long,
  routeCount: Long,
  changeCount: Long,
  active: Boolean
)
