package kpn.shared.data

case class RelationMember(relation: Relation, role: Option[String]) extends Member {
  override def isRelation: Boolean = true
}
