package kpn.api.common.data.raw

import kpn.api.common.data.MemberType
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class RawRelation(
  id: Long,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  members: Seq[RawMember],
  tags: Seq[Tag]
) extends RawElement {

  override def isRelation: Boolean = true

  def nodeMembers: Seq[RawMember] = members.filter(_.memberType == MemberType.Node)

  def wayMembers: Seq[RawMember] = members.filter(_.memberType == MemberType.Way)

  def relationMembers: Seq[RawMember] = members.filter(_.memberType == MemberType.Relation)
}
