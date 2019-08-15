package kpn.shared.network

import kpn.shared.NetworkFact

case class NetworkFactsPage(
  networkSummary: NetworkSummary,
  facts: Seq[NetworkFact]
)
