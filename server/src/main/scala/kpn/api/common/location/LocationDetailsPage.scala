package kpn.api.common.location

import kpn.api.common.LocationInfo

case class LocationDetailsPage(
  summary: LocationSummary,
  locationInfos: Seq[LocationInfo]
)
