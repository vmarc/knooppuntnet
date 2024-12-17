package kpn.api.common.data

import kpn.api.common.data.raw.RawMember

case class NodeMember(node: Node, role: Option[String]) extends Member {
  override def isNode: Boolean = true

  def toRaw: RawMember = {
    RawMember(
      MemberType.Node,
      node.id,
      role
    )
  }
}
