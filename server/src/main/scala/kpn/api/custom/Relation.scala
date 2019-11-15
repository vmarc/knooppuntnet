package kpn.api.custom

import kpn.api.common.data.Element
import kpn.api.common.data.Member
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.WayMember
import kpn.api.common.data.raw.RawRelation

case class Relation(raw: RawRelation, members: Seq[Member]) extends Element {

  def id: Long = raw.id

  def version: Long = raw.version

  def timestamp: Timestamp = raw.timestamp

  def changeSetId: Long = raw.changeSetId

  def tags: Tags = raw.tags

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

  def relationMember(id: Long): RelationMember = {
    relationMembers.find(m => m.relation.id == id).get
  }
}
