package kpn.api.common.route

import kpn.api.common.RouteType

case class RouteMapInfo(
  routeId: Long,
  routeName: String,
  routeType: RouteType,
  segments: Seq[RouteSegment],
  paths: Seq[RoutePath],
)
