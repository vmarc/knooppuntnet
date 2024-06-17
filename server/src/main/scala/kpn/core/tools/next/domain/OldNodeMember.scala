package kpn.core.tools.next.domain

case class OldNodeMember(node: OldNode, role: Option[String]) extends OldMember {
  override def isNode: Boolean = true
}
