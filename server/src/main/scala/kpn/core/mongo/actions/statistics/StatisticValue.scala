package kpn.core.mongo.actions.statistics

import kpn.api.custom.Country
import kpn.api.custom.NetworkType

case class StatisticValue(country: Country, networkType: NetworkType, name: String, value: Long)
