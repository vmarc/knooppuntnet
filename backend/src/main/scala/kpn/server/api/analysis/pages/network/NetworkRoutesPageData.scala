package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkSummary
import kpn.api.id.Storable
import kpn.core.doc.NetworkRouteDetail

case class NetworkRoutesPageData(
  summary: NetworkSummary,
  routes: Seq[NetworkRouteDetail]
) extends Storable
