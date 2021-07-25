package kpn.api.common.data.raw

import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class RawRelation(
  id: Long,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  members: Seq[RawMember],
  tags: Tags
) extends RawElement {

  override def isRelation: Boolean = true

  def nodeMembers: Seq[RawMember] = members.filter(_.memberType == "node")

  def wayMembers: Seq[RawMember] = members.filter(_.memberType == "way")

  def relationMembers: Seq[RawMember] = members.filter(_.memberType == "relation")
}
