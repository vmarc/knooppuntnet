package kpn.api.common.network

import kpn.api.common.Bounds

case class NetworkMapPage(
  summary: NetworkSummary,
  nodes: Seq[NetworkMapNode],
  networkNodeIds: Seq[Long],
  connectionNodeIds: Seq[Long],
  networkRouteIds: Seq[Long],
  connectionRouteIds: Seq[Long],
  bounds: Bounds
)
