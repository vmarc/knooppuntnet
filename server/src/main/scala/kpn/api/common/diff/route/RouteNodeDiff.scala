package kpn.api.common.diff.route

import kpn.api.common.common.Ref
import kpn.api.common.common.ReferencedElements

case class RouteNodeDiff(
  title: String,
  added: Seq[Ref],
  removed: Seq[Ref]
) {

  def referencedNodeIds: Set[Long] = {
    added.map(_.id).toSet ++ removed.map(_.id).toSet
  }

  def referencedElements: ReferencedElements = {
    ReferencedElements(nodeIds = referencedNodeIds)
  }
}
