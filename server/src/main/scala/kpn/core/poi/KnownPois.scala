package kpn.core.poi

case class KnownPois(
  nodeIds: Set[Long] = Set.empty,
  wayIds: Set[Long] = Set.empty,
  relationIds: Set[Long] = Set.empty
)
