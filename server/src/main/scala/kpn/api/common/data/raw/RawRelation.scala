package kpn.api.common.data.raw

import kpn.api.common.common.ToStringBuilder
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

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("id", id).
    field("version", version).
    field("timestamp", timestamp).
    field("changeSetId", changeSetId).
    field("members", members).
    field("tags", tags).
    build
}
