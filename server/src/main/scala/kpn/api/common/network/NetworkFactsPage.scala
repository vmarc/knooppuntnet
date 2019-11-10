package kpn.api.common.network

import kpn.api.common.NetworkFact

case class NetworkFactsPage(
  networkSummary: NetworkSummary,
  facts: Seq[NetworkFact]
)
