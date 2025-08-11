package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType

object Label {

  val survey: String = "survey"
  val facts: String = "facts"
  val orphan: String = "orphan"

  def routeType(routeType: RouteType): String = s"network-type-${routeType.toString}"

  def location(locationName: String): String = s"location-$locationName"

  def country(country: Country): String = location(country.toString)

  def fact(fact: Fact): String = s"fact-${fact.toString}"

  def scope(routeScope: RouteScope): String = s"scope-${routeScope.toString}"
}
