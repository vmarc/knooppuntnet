package kpn.server.analyzer.engine.analysis.route.structure

case class RoutePath(
  id: Long,
  direction: RoutePathDirection,
  elements: Seq[NewRouteSegmentElement]
)
