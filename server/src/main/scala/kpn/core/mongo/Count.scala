package kpn.core.mongo

import kpn.api.custom.Country
import kpn.api.custom.NetworkType

case class Count(country: Country, networkType: NetworkType, name: String, count: Long)