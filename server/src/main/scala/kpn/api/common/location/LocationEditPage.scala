package kpn.api.common.location

import kpn.api.common.Bounds
import kpn.api.common.TimeInfo

case class LocationEditPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  bounds: Bounds,
  nodeIds: Seq[Long],
  routeIds: Seq[Long]
)
