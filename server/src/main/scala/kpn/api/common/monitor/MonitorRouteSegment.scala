package kpn.api.common.monitor

import kpn.api.common.BoundsI

case class MonitorRouteSegment(
  id: Long,
  meters: Long,
  bounds: BoundsI,
  geoJson: String
)
