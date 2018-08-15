package kpn.shared.subset

import kpn.shared.Fact

case class SubsetFactDetailsPage(subsetInfo: SubsetInfo, fact: Fact, networks: Seq[NetworkRoutesFacts]) {
  def routeCount: Int = networks.map { n => n.facts.routes.size }.sum
}
