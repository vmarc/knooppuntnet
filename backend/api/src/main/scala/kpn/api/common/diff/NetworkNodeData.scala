package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawNode

case class NetworkNodeData(
  node: RawNode,
  name: String
) {

  def id: Long = node.id

  def toRef: Ref = Ref(node.id, name)

}
