package kpn.api.common.route

import kpn.api.custom.NetworkType

case class RouteMapInfo(
  routeId: Long,
  routeName: String,
  networkType: NetworkType,
  segments: Seq[RouteSegment],
  paths: Seq[RoutePath],
)
