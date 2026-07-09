package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkSummary
import kpn.core.doc.Storable

case class NetworkChangesPageData(
  summary: NetworkSummary
) extends Storable
