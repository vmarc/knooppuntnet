package kpn.api.common.route

import kpn.api.common.NetworkType

case class RouteMapInfo(
  routeId: Long,
  routeName: String,
  networkType: NetworkType,
  segments: Seq[RouteSegment],
  paths: Seq[RoutePath],
)
