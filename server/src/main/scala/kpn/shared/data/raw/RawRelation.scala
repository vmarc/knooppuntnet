package kpn.shared.data.raw

import kpn.shared.Timestamp
import kpn.shared.data.Tags

case class RawRelation(
  id: Long,
  version: Int,
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
