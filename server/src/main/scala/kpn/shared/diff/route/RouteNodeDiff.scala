package kpn.shared.diff.route

import kpn.shared.common.Ref
import kpn.shared.common.ReferencedElements

case class RouteNodeDiff(
  title: String,
  added: Seq[Ref],
  removed: Seq[Ref]
) {

  def referencedElements: ReferencedElements = {
    val nodeIds = added.map(_.id) ++ removed.map(_.id)
    ReferencedElements(nodeIds = nodeIds.toSet)
  }
}
