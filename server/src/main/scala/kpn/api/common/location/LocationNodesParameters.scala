package kpn.api.common.location

import kpn.api.custom.LocationNodesType

case class LocationNodesParameters(
  locationNodesType: LocationNodesType,
  pageSize: Long = 5,
  pageIndex: Long = 0
)
