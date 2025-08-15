package kpn.api.common.network

import kpn.api.common.NetworkFact
import kpn.core.doc.Storable

case class NetworkFactsPage(
  _id: Long,
  summary: NetworkSummary,
  facts: Seq[NetworkFact]
) extends Storable
