package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkSummary
import kpn.core.doc.NetworkInfoRouteDetail

case class NetworkRoutesPageData(
  summary: NetworkSummary,
  routes: Seq[NetworkInfoRouteDetail]
)
