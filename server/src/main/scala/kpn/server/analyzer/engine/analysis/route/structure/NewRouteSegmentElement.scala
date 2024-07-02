package kpn.server.analyzer.engine.analysis.route.structure

import kpn.server.analyzer.engine.analysis.route.RouteNodeData

case class NewRouteSegmentElement(
  id: Long,
  direction: RoutePathDirection,
  fromNetworkNode: Option[RouteNodeData],
  toNetworkNode: Option[RouteNodeData],
  fromNodeId: Long,
  toNodeId: Long,
  links: Seq[RouteLinkWay]
)
