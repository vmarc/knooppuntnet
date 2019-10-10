package kpn.shared.diff

import kpn.shared.Subset
import kpn.shared.common.ReferencedElements

object NodeDiffs {
  def empty: NodeDiffs = NodeDiffs()
}

case class NodeDiffs(
  removed: Seq[NodeData] = Seq.empty,
  added: Seq[NodeData] = Seq.empty,
  updated: Seq[NodeDataUpdate] = Seq.empty
) {

  def subsets: Seq[Subset] = {
    Seq(
      removed.flatMap(_.subsets),
      added.flatMap(_.subsets),
      updated.flatMap(_.before.subsets),
      updated.flatMap(_.after.subsets)
    ).flatten.distinct.sorted
  }

  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || updated.nonEmpty

  def referencedElements: ReferencedElements = {
    val nodeIds = removed.map(_.id) ++ added.map(_.id) ++ updated.map(_.id)
    ReferencedElements(nodeIds = nodeIds.toSet)
  }
}
