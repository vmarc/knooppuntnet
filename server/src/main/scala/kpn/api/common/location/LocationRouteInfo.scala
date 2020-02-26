package kpn.api.common.location

import kpn.api.custom.Timestamp

case class LocationRouteInfo(
  id: Long,
  name: String,
  meters: Long,
  lastUpdated: Timestamp,
  broken: Boolean
)
