package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.NetworkFact
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkSummary
import org.bson.types.ObjectId

case class NetworkDoc(
  _id: Long,
  active: Boolean,
  country: Option[Country],
  summary: NetworkSummary,
  detail: NetworkDetail,
  facts: Seq[NetworkFact],
  nodes: Seq[NetworkInfoNodeDetail],
  routes: Seq[NetworkRouteDetail],
  extraNodeIds: Seq[Long],
  extraWayIds: Seq[Long],
  extraRelationIds: Seq[Long],
  members: Seq[RawMember],
  stamp: Option[ObjectId],
) extends WithId {

  def memberNodeIds: Seq[Long] = {
    membersTypeRefs(MemberType.Node)
  }

  def memberWayIds: Seq[Long] = {
    membersTypeRefs(MemberType.Way)
  }

  def memberRelationIds: Seq[Long] = {
    membersTypeRefs(MemberType.Relation)
  }

  def nodeMembers: Seq[RawMember] = {
    membersType(MemberType.Node)
  }

  def wayMembers: Seq[RawMember] = {
    membersType(MemberType.Way)
  }

  def relationMembers: Seq[RawMember] = {
    membersType(MemberType.Relation)
  }

  private def membersType(memberType: MemberType): Seq[RawMember] = {
    members.filter(_.memberType == memberType)
  }

  private def membersTypeRefs(memberType: MemberType): Seq[Long] = {
    membersType(memberType).map(_.ref)
  }
}
