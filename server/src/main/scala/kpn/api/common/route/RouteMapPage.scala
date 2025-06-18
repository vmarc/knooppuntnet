package kpn.api.common.route

import kpn.api.common.Bounds

case class RouteMapPage(
  routeInfo: RouteInfo,
  routeMapInfo: RouteMapInfo,
  bounds: Bounds,
)
