package kpn.api.common.statistics

import kpn.api.common.Country
import kpn.api.common.NetworkType

case class StatisticValue(country: Country, networkType: NetworkType, value: String)
