package kpn.api.common.network

case class NetworkMapPage(
  networkSummary: NetworkSummary,
  nodes: Seq[NetworkInfoNode],
  nodeIds: Seq[Long],
  routeIds: Seq[Long]
)
