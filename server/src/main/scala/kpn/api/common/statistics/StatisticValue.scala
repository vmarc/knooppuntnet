package kpn.api.common.statistics

import kpn.api.common.NetworkType
import kpn.api.custom.Country

case class StatisticValue(country: Country, networkType: NetworkType, value: String)
