package kpn.api.common.location

import kpn.api.custom.Timestamp

case class LocationRouteInfo(
  id: Long,
  name: String,
  length: Long,
  investigate: Boolean,
  accessible: Boolean,
  relationLastUpdated: Timestamp
)
