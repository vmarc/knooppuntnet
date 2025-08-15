package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkSummary
import kpn.core.doc.NetworkRouteDetail
import kpn.core.doc.Storable

case class NetworkRoutesPageData(
  summary: NetworkSummary,
  routes: Seq[NetworkRouteDetail]
) extends Storable
