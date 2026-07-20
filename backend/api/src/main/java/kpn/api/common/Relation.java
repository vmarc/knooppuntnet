package kpn.api.common;

import kpn.api.common.data.Element;
import kpn.api.common.data.Member;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;

public record Relation(
  Long id,
  Long version,
  Timestamp timestamp,
  Long changeSetId,
  ImmutableList<Tag> tags,
  ImmutableList<Member> members
) implements Element {
}

/* TODO migrate
package kpn.api.common

import kpn.api.common.data.Element
import kpn.api.common.data.Member
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

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

*/
