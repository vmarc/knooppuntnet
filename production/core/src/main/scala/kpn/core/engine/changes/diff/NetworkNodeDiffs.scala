package kpn.core.engine.changes.diff

import kpn.shared.common.ReferencedElements
import kpn.shared.diff.NetworkNodeData
import kpn.shared.diff.NetworkNodeUpdate
import kpn.shared.diff.RefDiffs

object NetworkNodeDiffs {
  def empty = NetworkNodeDiffs()
}

case class NetworkNodeDiffs(
  removed: Seq[NetworkNodeData] = Seq(),
  added: Seq[NetworkNodeData] = Seq(),
  updated: Seq[NetworkNodeUpdate] = Seq()
) {

  def referencedElements: ReferencedElements = {
    val nodeIds = removed.map(_.id) ++ added.map(_.id) ++ updated.map(_.id)
    ReferencedElements(nodeIds = nodeIds.toSet)
  }

  def toRefDiffs: RefDiffs = RefDiffs(
    removed.map(_.toRef),
    added.map(_.toRef),
    updated.map(_.toRef)
  )
}
