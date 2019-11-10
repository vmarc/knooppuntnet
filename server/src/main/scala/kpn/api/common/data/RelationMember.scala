package kpn.api.common.data

import kpn.api.custom.Relation

case class RelationMember(relation: Relation, role: Option[String]) extends Member {
  override def isRelation: Boolean = true
}
