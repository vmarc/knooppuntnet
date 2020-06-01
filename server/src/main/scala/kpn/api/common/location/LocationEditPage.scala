package kpn.api.common.location

import kpn.api.common.Bounds
import kpn.api.common.TimeInfo
import kpn.api.common.common.Ref

case class LocationEditPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  bounds: Bounds,
  nodeRefs: Seq[Ref],
  routeRefs: Seq[Ref]
)
