package kpn.api.common.route

import kpn.api.common.RouteType

case class RouteInfo(
  routeId: Long,
  routeName: String,
  routeTypes: Seq[RouteType],
  changeCount: Long,
  segmentCount: Long,
)
