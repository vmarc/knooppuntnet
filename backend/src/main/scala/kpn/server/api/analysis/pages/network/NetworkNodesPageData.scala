package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkSummary
import kpn.core.doc.Storable

case class NetworkNodesPageData(
  summary: NetworkSummary,
  nodes: Seq[NetworkNodeDetail]
) extends Storable
