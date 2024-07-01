package kpn.server.analyzer.engine.analysis.route.structure

case class NewRouteSegmentElement(
  id: Long,
  direction: RoutePathDirection,
  fromNodeId: Long,
  toNodeId: Long,
  links: Seq[RouteLinkWay]
)
