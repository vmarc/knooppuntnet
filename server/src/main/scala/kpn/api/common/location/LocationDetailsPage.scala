package kpn.api.common.location

import kpn.api.common.LocationInfo
import kpn.api.custom.Tags

case class LocationDetailsPage(
  summary: LocationSummary,
  relationId: Long,
  distance: Long,
  locationInfos: Seq[LocationInfo],
  tags: Tags
)
