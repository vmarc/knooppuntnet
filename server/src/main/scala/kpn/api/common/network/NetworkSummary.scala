package kpn.api.common.network

import kpn.api.common.NetworkScope
import kpn.api.common.RouteType

case class NetworkSummary(
  name: String,
  routeType: RouteType,
  networkScope: NetworkScope,
  factCount: Long,
  nodeCount: Long,
  routeCount: Long,
  changeCount: Long
)
