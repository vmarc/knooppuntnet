package kpn.api.common.diff.route

import kpn.api.common.common.Ref
import kpn.api.common.common.ReferencedElements
import kpn.api.common.common.ToStringBuilder

case class RouteNodeDiff(
  title: String,
  added: Seq[Ref],
  removed: Seq[Ref]
) {

  def referencedElements: ReferencedElements = {
    val nodeIds = added.map(_.id) ++ removed.map(_.id)
    ReferencedElements(nodeIds = nodeIds.toSet)
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("title", title).
    field("added", added).
    field("removed", removed).
    build
}
