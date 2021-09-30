package kpn.core.doc

import kpn.api.custom.Fact
import kpn.api.custom.NetworkType

object Label {

  val active: String = "active"
  val survey: String = "survey"
  val facts: String = "facts"

  def networkType(networkType: NetworkType): String = s"network-type-${networkType.name}"

  def location(locationName: String): String = s"location-$locationName"

  def fact(fact: Fact): String = s"fact-${fact.name}"
}
