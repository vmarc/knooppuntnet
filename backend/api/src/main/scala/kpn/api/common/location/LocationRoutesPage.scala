package kpn.api.common.location

import kpn.api.common.TimeInfo

case class LocationRoutesPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  routeCount: Long,
  filter: LocationRouteOptions,
  routes: Seq[LocationRouteInfo]
)
