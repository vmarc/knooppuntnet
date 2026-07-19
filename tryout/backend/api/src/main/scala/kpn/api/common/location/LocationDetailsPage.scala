package kpn.api.common.location

import kpn.api.common.LocationInfo
import kpn.api.custom.Tag

case class LocationDetailsPage(
  summary: LocationSummary,
  relationId: Long,
  distance: Long,
  locationInfos: Seq[LocationInfo],
  tags: Seq[Tag]
)
