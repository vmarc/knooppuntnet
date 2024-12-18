package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkType
import kpn.api.common.RouteScope

object Label {

  val active: String = "active"
  val survey: String = "survey"
  val facts: String = "facts"

  def networkType(networkType: NetworkType): String = s"network-type-${networkType.entryName}"

  def location(locationName: String): String = s"location-$locationName"

  def country(country: Country): String = location(country.entryName)

  def fact(fact: Fact): String = s"fact-${fact.entryName}"

  def scope(routeScope: RouteScope): String = s"scope-${routeScope.entryName}"
}
