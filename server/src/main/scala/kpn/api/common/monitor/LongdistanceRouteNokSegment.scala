package kpn.api.common.monitor

import kpn.api.common.BoundsI

case class LongdistanceRouteNokSegment(
  id: Long,
  meters: Long,
  distance: Long,
  bounds: BoundsI,
  geoJson: String
)
