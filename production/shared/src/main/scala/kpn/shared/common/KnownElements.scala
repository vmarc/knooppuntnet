package kpn.shared.common

case class KnownElements(
  nodeIds: Set[Long] = Set(),
  routeIds: Set[Long] = Set()
)
