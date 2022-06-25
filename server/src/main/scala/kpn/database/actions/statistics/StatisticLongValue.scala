package kpn.database.actions.statistics

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset

case class StatisticLongValue(country: Country, networkType: NetworkType, value: Long) {
  def isSubset(subset: Subset): Boolean = {
    country == subset.country && networkType == subset.networkType
  }
}
