package kpn.database.actions.statistics

import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.custom.Subset
import kpn.core.doc.Storable

case class StatisticLongValue(country: Country, routeType: RouteType, value: Long) extends Storable {
  def isSubset(subset: Subset): Boolean = {
    country == subset.country && routeType == subset.routeType
  }
}
