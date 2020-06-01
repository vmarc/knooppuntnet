package kpn.api.common.location

import kpn.api.common.Bounds
import kpn.api.common.TimeInfo

case class LocationSummaryPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  bounds: Bounds,
  nodeIds: Seq[Ids],
  routeIds: Seq[Ids]
)
