package kpn.api.common.location

import kpn.api.common.TimeInfo

case class LocationRoutesPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  routeCount: Long,
  allRouteCount: Long,
  factsRouteCount: Long,
  inaccessibleRouteCount: Long,
  surveyRouteCount: Long,
  routes: Seq[LocationRouteInfo]
)
