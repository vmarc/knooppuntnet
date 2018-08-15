package kpn.shared.diff

import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.data.raw.RawNode

case class NodeData(
  subsets: Seq[Subset],
  name: String,
  node: RawNode
) {

  def id: Long = node.id

  def toRef: Ref = Ref(id, name)
}
