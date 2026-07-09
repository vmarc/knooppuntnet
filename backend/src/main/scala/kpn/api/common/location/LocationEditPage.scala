package kpn.api.common.location

import kpn.api.common.Bounds
import kpn.api.common.TimeInfo

case class LocationEditPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  tooManyNodes: Boolean,
  maxNodes: Long,
  bounds: Bounds,
  nodeIds: Seq[Long],
  routeIds: Seq[Long]
)
