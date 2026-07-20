package kpn.api.common.data;

import kpn.api.common.Relation;

import java.util.Optional;

public record Member(
  Optional<Node> node,
  Optional<Way> way,
  Optional<Relation> relation,
  Optional<Long> relationId,
  Optional<String> role
) {}

/* TODO migrate
package kpn.api.common.data

import kpn.api.common.Relation
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Tag
import kpn.api.custom.Tags

case class Member(
  node: Option[Node] = None,
  way: Option[Way] = None,
  relation: Option[Relation] = None,
  relationId: Option[Long] = None,
  role: Option[String] = None,
) {

  def isNode: Boolean = {
    node.isDefined
  }

  def isWay: Boolean = {
    way.isDefined
  }

  def isRelation: Boolean = {
    relation.isDefined
  }

  def isRelationId: Boolean = {
    relationId.isDefined
  }

  def wayNodes: Seq[Node] = {
    way.toSeq.flatMap(_.nodes)
  }

  def tags: Seq[Tag] = {
    node.map(_.tags)
      .orElse(way.map(_.tags))
      .orElse(relation.map(_.tags))
      .getOrElse(Seq.empty)
  }

  def hasTag(key: String, allowedValues: String*): Boolean = {
    Tags.has(tags, key, allowedValues *)
  }

  def toRaw: RawMember = {
    RawMember(
      memberType,
      memberId,
      role
    )
  }

  def memberId: Long = {
    relationId.orElse(
      node.map(_.id).orElse(
        way.map(_.id).orElse(
          relation.map(_.id)
        )
      )
    ).getOrElse(0L)
  }

  private def memberType: MemberType = {
    if (isRelation || isRelationId) {
      MemberType.Relation
    } else if (isNode) {
      MemberType.Node
    } else if (isWay) {
      MemberType.Way
    } else {
      throw new IllegalStateException("Member must have a type (node, way, or relation)")
    }
  }
}

*/
