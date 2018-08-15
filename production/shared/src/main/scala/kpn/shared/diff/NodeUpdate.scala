package kpn.shared.diff

import kpn.shared.data.raw.RawNode
import kpn.shared.diff.node.NodeMoved

case class NodeUpdate(
  before: RawNode,
  after: RawNode,
  tagDiffs: Option[TagDiffs] = None,
  nodeMoved: Option[NodeMoved] = None
) {

  def id: Long = before.id

  def nonEmpty: Boolean = tagDiffs.isDefined || nodeMoved.isDefined

}
