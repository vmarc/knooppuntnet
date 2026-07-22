package kpn.api.common.diff

import kpn.api.common.data.Node
import kpn.api.common.diff.node.NodeMoved

case class NodeUpdate(
  before: Node,
  after: Node,
  tagDiffs: Option[TagDiffs] = None,
  nodeMoved: Option[NodeMoved] = None
) {

  def id: Long = before.id

  def nonEmpty: Boolean = tagDiffs.isDefined || nodeMoved.isDefined
}
