package kpn.api.custom

import kpn.api.common.Country
import kpn.api.common.NetworkType

case class LocationKey(
  networkType: NetworkType,
  country: Country,
  name: String
)
