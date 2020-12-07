package kpn.api.common.longdistance

import kpn.api.common.BoundsI

case class LongDistanceRouteSegment(
  id: Long,
  meters: Long,
  bounds: BoundsI,
  geoJson: String
)
