package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkSummary
import kpn.api.id.Storable

case class NetworkChangesPageData(
  summary: NetworkSummary
) extends Storable
