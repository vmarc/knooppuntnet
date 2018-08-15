package kpn.shared.diff

import kpn.shared.Country
import kpn.shared.common.Ref
import kpn.shared.data.raw.RawNode

case class NetworkNodeData(
  node: RawNode,
  name: String,
  country: Option[Country]
) {

  def id: Long = node.id

  def toRef: Ref = Ref(node.id, name)

}
