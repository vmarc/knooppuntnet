package kpn.api.common.statistics

import kpn.api.common.Country
import kpn.api.common.RouteType

case class StatisticValue(country: Country, routeType: RouteType, value: String)
