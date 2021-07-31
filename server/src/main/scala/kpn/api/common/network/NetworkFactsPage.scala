package kpn.api.common.network

import kpn.api.common.NetworkFact

case class NetworkFactsPage(
  _id: Long,
  summary: NetworkSummary,
  facts: Seq[NetworkFact]
)
