package kpn.api.common.location

import kpn.api.common.TimeInfo

case class LocationRoutesPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  routes: Seq[LocationRouteInfo]
)
