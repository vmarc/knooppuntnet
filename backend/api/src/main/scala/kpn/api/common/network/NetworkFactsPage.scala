package kpn.api.common.network

import kpn.api.common.NetworkFact
import kpn.api.id.Storable

case class NetworkFactsPage(
  summary: NetworkSummary,
  facts: Seq[NetworkFact]
) extends Storable
