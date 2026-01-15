package kpn.server.api.analysis.pages.network

import kpn.api.common.NetworkFact
import kpn.api.common.network.NetworkSummary
import kpn.core.doc.Storable

case class NetworkFactsPageData(
  summary: NetworkSummary,
  facts: Seq[NetworkFact]
) extends Storable
