package kpn.api.custom

import kpn.api.common.Country
import kpn.api.common.RouteType

case class LocationKey(
  routeType: RouteType,
  country: Country,
  name: String
)
