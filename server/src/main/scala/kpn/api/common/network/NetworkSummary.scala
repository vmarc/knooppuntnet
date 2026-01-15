package kpn.api.common.network

import kpn.api.common.RouteScope
import kpn.api.common.RouteType

case class NetworkSummary(
  name: Option[String],
  routeType: RouteType,
  routeScope: RouteScope,
  factCount: Long,
  nodeCount: Long,
  routeCount: Long,
  changeCount: Long
)
