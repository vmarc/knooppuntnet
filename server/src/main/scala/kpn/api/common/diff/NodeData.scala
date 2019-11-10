package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Subset

case class NodeData(
  subsets: Seq[Subset],
  name: String,
  node: RawNode
) {

  def id: Long = node.id

  def toRef: Ref = Ref(id, name)
}
