package kpn.api.common.longdistance

import kpn.api.common.Bounds

case class LongDistanceRouteSegment(
  id: Long,
  meters: Long,
  bounds: Bounds,
  geoJson: String
)
