package kpn.api.common.diff;

import kpn.api.common.data.Node;
import kpn.api.common.diff.TagDiffs;
import kpn.api.common.diff.node.NodeMoved;

import java.util.Optional;

public record NodeUpdate(
  Node before,
  Node after,
  Optional<TagDiffs> tagDiffs,
  Optional<NodeMoved> nodeMoved
) {}

/* TODO migrate
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

*/
