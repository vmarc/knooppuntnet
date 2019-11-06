package kpn.server.analyzer.engine.context

case class Elements(
  nodeIds: Set[Long] = Set.empty,
  routeIds: Set[Long] = Set.empty,
  networkIds: Set[Long] = Set.empty
)
