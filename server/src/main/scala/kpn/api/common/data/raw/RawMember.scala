package kpn.api.common.data.raw

import kpn.api.common.common.ToStringBuilder

case class RawMember(memberType: String, ref: Long, role: Option[String]) {

  def isNode: Boolean = memberType == "node"

  def isWay: Boolean = memberType == "way"

  def isRelation: Boolean = memberType == "relation"

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("memberType", memberType).
    field("ref", ref).
    field("role", role).
    build
}
