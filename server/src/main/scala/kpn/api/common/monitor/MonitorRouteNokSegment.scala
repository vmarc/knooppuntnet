package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorRouteNokSegment(
  id: Long,
  meters: Long,
  distance: Long,
  bounds: Bounds,
  geoJson: String
)
