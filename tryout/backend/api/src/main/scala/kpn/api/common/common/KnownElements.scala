package kpn.api.common.common

case class KnownElements(
  nodeIds: Set[Long] = Set.empty,
  routeIds: Set[Long] = Set.empty
)
