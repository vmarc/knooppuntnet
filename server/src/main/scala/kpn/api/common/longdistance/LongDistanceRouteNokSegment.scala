package kpn.api.common.longdistance

import kpn.api.common.Bounds

case class LongDistanceRouteNokSegment(
  id: Long,
  meters: Long,
  distance: Long,
  bounds: Bounds,
  geoJson: String
)
