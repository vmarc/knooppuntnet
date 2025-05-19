package kpn.api.custom

import kpn.api.common.data.Element
import kpn.api.common.data.Member
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationIdMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
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

  def nodeMembers: Seq[NodeMember] = {
    members.flatMap {
      case nodeMember: NodeMember => Some(nodeMember)
      case _ => None
    }
  }

  def wayMembers: Seq[WayMember] = {
    members.flatMap {
      case wayMember: WayMember => Some(wayMember)
      case _ => None
    }
  }

  def relationMembers: Seq[RelationMember] = {
    members.flatMap {
      case relationMember: RelationMember => Some(relationMember)
      case _ => None
    }
  }

  def relationIdMembers: Seq[RelationIdMember] = {
    members.flatMap {
      case relationMember: RelationIdMember => Some(relationMember)
      case _ => None
    }
  }

  def relationMember(id: Long): RelationMember = {
    relationMembers.find(m => m.relation.id == id).get
  }

  def ways: Seq[Way] = {
    wayMembers.map(_.way)
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
