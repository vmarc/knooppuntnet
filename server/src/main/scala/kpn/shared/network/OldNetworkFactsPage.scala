package kpn.shared.network

import kpn.shared.Fact
import kpn.shared.NetworkFacts

case class OldNetworkFactsPage(
  networkSummary: NetworkSummary,
  networkFacts: NetworkFacts,
  nodeFacts: Seq[NetworkNodeFact],
  routeFacts: Seq[NetworkRouteFact],
  facts: Seq[Fact]
) {

  def factCount: Int = {
    networkFacts.factCount +
      nodeFacts.map(_.nodes.size).sum +
      routeFacts.map(_.routes.size).sum +
      facts.size
  }

}
