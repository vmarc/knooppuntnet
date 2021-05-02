package kpn.api.common.location

import kpn.api.custom.LocationNodesType

case class LocationNodesParameters(
  locationNodesType: LocationNodesType,
  itemsPerPage: Long = 5,
  pageIndex: Long = 0
)
