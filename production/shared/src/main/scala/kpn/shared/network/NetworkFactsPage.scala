package kpn.shared.network

import kpn.shared.Fact
import kpn.shared.NetworkFacts

case class NetworkFactsPage(
  networkSummary: NetworkSummary,
  networkFacts: NetworkFacts,
  routeFacts: Seq[NetworkRouteFact],
  facts: Seq[Fact]
) {

  def factCount: Int = networkFacts.factCount + routeFacts.map(_.routes.size).sum + facts.size

}

