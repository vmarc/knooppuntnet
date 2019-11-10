package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Country

case class NetworkNodeData(
  node: RawNode,
  name: String,
  country: Option[Country]
) {

  def id: Long = node.id

  def toRef: Ref = Ref(node.id, name)

}
