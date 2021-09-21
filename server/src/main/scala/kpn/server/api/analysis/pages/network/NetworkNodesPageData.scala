package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkSummary

case class NetworkNodesPageData(
  summary: NetworkSummary,
  nodes: Seq[NetworkNodeDetail]
)
