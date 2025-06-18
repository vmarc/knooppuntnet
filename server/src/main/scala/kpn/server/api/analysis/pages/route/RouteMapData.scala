package kpn.server.api.analysis.pages.route

import kpn.api.common.RouteType
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment

case class RouteMapData(
  routeId: Long,
  routeName: String,
  routeTypes: Seq[RouteType],
  segments: Seq[RouteSegment],
  paths: Seq[RoutePath],
)
