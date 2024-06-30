package kpn.server.analyzer.engine.analysis.route.structure

case class RoutePath(
  id: Long,
  direction: RoutePathDirection,
  fromNodeId: Long,
  toNodeId: Long,
  links: Seq[RouteLinkWay]
)
