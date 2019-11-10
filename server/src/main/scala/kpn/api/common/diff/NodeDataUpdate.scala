package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.Subset

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
