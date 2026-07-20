package kpn.api.common.data.raw;

import kpn.api.common.data.MemberType;

import java.util.Optional;

public record RawMember(
  MemberType memberType,
  Long ref,
  Optional<String> role
) {}

/* TODO migrate
package kpn.api.common.data.raw

import kpn.api.common.data.MemberType

case class RawMember(memberType: MemberType, ref: Long, role: Option[String]) {

  def isNode: Boolean = memberType == MemberType.Node

  def isWay: Boolean = memberType == MemberType.Way

  def isRelation: Boolean = memberType == MemberType.Relation
}

*/
