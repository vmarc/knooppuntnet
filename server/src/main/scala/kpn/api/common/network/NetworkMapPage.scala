package kpn.api.common.network

import kpn.api.common.Bounds

case class NetworkMapPage(
  networkSummary: NetworkSummary,
  nodes: Seq[NetworkMapNode],
  nodeIds: Seq[Long],
  routeIds: Seq[Long],
  bounds: Bounds
)
