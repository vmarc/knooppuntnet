package kpn.api.common.location

import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class LocationRouteInfo(
  id: Long,
  name: String,
  meters: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  broken: Boolean,
  unaccessible: Boolean
)
