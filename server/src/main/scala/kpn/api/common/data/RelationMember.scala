package kpn.api.common.data

import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Relation

case class RelationMember(relation: Relation, role: Option[String]) extends Member {
  override def isRelation: Boolean = true

  def toRaw: RawMember = {
    RawMember(
      MemberType.Relation,
      relation.id,
      role
    )
  }
}
