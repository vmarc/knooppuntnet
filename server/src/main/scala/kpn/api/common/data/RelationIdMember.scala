package kpn.api.common.data

import kpn.api.common.data.raw.RawMember

case class RelationIdMember(relationId: Long, role: Option[String]) extends Member {
  override def isRelationId: Boolean = true

  def toRaw: RawMember = {
    RawMember(
      "relation",
      relationId,
      role
    )
  }
}
