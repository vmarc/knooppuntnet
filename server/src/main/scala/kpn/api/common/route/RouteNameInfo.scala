package kpn.api.common.route

import kpn.api.common.RouteType

case class RouteNameInfo(
  routeId: Long,
  routeName: String,
  routeType: RouteType,
  segmentCount: Long,
)
