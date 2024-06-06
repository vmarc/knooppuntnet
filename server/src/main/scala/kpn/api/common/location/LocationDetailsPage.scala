package kpn.api.common.location

import kpn.api.common.LocationInfo

case class LocationDetailsPage(
  summary: LocationSummary,
  distance: Long,
  locationInfos: Seq[LocationInfo]
)
