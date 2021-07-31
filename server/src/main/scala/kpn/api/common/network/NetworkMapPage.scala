package kpn.api.common.network

import kpn.api.common.Bounds

case class NetworkMapPage(
  summary: NetworkSummary,
  nodes: Seq[NetworkMapNode],
  nodeIds: Seq[Long],
  routeIds: Seq[Long],
  bounds: Bounds
)
