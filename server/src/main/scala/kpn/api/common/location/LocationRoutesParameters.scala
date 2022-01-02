package kpn.api.common.location

import kpn.api.custom.LocationRoutesType

case class LocationRoutesParameters(
  locationRoutesType: LocationRoutesType,
  pageSize: Long = 5,
  pageIndex: Long = 0
)
