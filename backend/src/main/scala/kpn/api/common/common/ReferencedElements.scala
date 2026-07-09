package kpn.api.common.common

object ReferencedElements {
  def merge(xs: ReferencedElements*): ReferencedElements = {
    val mergedNodeIds = xs.flatMap(_.nodeIds).toSet
    val mergedRouteIds = xs.flatMap(_.routeIds).toSet
    ReferencedElements(mergedNodeIds, mergedRouteIds)
  }
}

case class ReferencedElements(nodeIds: Set[Long] = Set.empty, routeIds: Set[Long] = Set.empty)
