package kpn.api.common.data

case class NodeMember(node: Node, role: Option[String]) extends Member {
  override def isNode: Boolean = true
}
