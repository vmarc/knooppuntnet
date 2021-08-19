package kpn.api.common.statistics

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset

case class StatisticValue(country: Country, networkType: NetworkType, value: Long) {
  def isSubset(subset: Subset): Boolean = {
    country == subset.country && networkType == subset.networkType
  }
}
