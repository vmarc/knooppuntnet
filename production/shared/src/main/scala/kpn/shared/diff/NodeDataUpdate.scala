package kpn.shared.diff

import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.diff.node.NodeMoved

case class NodeDataUpdate(
  before: NodeData,
  after: NodeData,
  tagDiffs: Option[TagDiffs] = None,
  nodeMoved: Option[NodeMoved] = None
) {

  def id: Long = before.id

  def toRef: Ref = Ref(id, after.name)

  def nonEmpty: Boolean = tagDiffs.isDefined || nodeMoved.isDefined

  def subsets: Seq[Subset] = (before.subsets ++ after.subsets).distinct.sorted

}
