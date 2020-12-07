package kpn.api.common.longdistance

import kpn.api.common.BoundsI

case class LongDistanceRouteNokSegment(
  id: Long,
  meters: Long,
  distance: Long,
  bounds: BoundsI,
  geoJson: String
)
