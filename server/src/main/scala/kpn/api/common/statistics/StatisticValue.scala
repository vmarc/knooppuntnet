package kpn.api.common.statistics

import kpn.api.custom.Country
import kpn.api.custom.NetworkType

case class StatisticValue(country: Country, networkType: NetworkType, value: Long)
