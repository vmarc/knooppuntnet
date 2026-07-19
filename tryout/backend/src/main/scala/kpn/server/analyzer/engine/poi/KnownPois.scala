package kpn.server.analyzer.engine.poi

case class KnownPois(
  nodeIds: Set[Long] = Set.empty,
  wayIds: Set[Long] = Set.empty,
  relationIds: Set[Long] = Set.empty
)
