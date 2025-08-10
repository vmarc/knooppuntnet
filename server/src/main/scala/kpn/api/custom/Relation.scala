package kpn.api.custom

import kpn.api.common.data.Element
import kpn.api.common.data.Member
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawRelation

case class Relation(
  id: Long,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  tags: Seq[Tag],
  members: Seq[Member]
) extends Element {

  override def isRelation: Boolean = true

  def nodeMembers: Seq[Member] = {
    members.filter(_.isNode)
  }

  def wayMembers: Seq[Member] = {
    members.filter(_.isWay)
  }

  def relationMembers: Seq[Member] = {
    members.filter(_.isRelation)
  }

  def relationIdMembers: Seq[Member] = {
    members.filter(_.isRelationId)
  }

  def relationMember(id: Long): Member = {
    relationMembers.find(m => m.relation.map(_.id).contains(id)).get
  }

  def ways: Seq[Way] = {
    wayMembers.flatMap(_.way)
  }

  def toRaw: RawRelation = {
    RawRelation(
      id,
      version,
      timestamp,
      changeSetId,
      members.map(_.toRaw),
      tags
    )
  }
}
