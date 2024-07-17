package kpn.api.common.route

import kpn.api.common.Bounds

case class RouteMapPage(
  routeMapInfo: RouteMapInfo,
  bounds: Bounds,
  changeCount: Long
)
