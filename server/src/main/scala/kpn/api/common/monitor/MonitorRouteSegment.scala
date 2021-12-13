package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorRouteSegment(
  id: Long,
  meters: Long,
  bounds: Bounds,
  geoJson: String
)
