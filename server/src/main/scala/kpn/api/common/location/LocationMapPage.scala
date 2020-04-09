package kpn.api.common.location

import kpn.api.common.Bounds

case class LocationMapPage(
  summary: LocationSummary,
  bounds: Bounds,
  geoJson: String
)
