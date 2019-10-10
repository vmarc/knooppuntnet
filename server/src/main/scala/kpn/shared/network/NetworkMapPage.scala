package kpn.shared.network

case class NetworkMapPage(
  networkSummary: NetworkSummary,
  nodes: Seq[NetworkNodeInfo2],
  nodeIds: Seq[Long],
  routeIds: Seq[Long]
)
