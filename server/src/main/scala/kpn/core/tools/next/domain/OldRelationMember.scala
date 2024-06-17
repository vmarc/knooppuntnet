package kpn.core.tools.next.domain

case class OldRelationMember(relation: OldRelation, role: Option[String]) extends OldMember {
  override def isRelation: Boolean = true
}
