package kpn.api.common.diff;

import kpn.api.common.data.Node;
import kpn.api.custom.Subset;

import com.google.common.collect.ImmutableList;

public record NodeData(
  ImmutableList<Subset> subsets,
  String name,
  Node node
) {
}

/* TODO migrate
package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.data.Node
import kpn.api.custom.Subset

case class NodeData(
  subsets: Seq[Subset],
  name: String,
  node: Node
) {

  def id: Long = node.id

  def toRef: Ref = Ref(id, name)
}

*/
