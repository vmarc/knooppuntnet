package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.Country
import kpn.api.common.NetworkFact
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkSummary

object NetworkDoc {

  def from(rawRelation: RawRelation): NetworkDoc = {
    throw new Error("implement")
    //    val nodeMembers = rawRelation.nodeMembers.map { member =>
    //      NetworkNodeMember(member.ref, member.role)
    //    }
    //    val wayMembers = rawRelation.wayMembers.map { member =>
    //      NetworkWayMember(member.ref, member.role)
    //    }
    //    val relationMembers = rawRelation.relationMembers.map { member =>
    //      NetworkRelationMember(member.ref, member.role)
    //    }
    //    NetworkDoc(
    //      rawRelation.id,
    //      active = true,
    //      rawRelation.version,
    //      rawRelation.changeSetId,
    //      rawRelation.timestamp,
    //      nodeMembers,
    //      wayMembers,
    //      relationMembers,
    //      rawRelation.tags
    //    )
  }
}

case class NetworkDoc(
  _id: Long,
  active: Boolean,
  country: Option[Country],
  summary: NetworkSummary,
  detail: NetworkDetail,
  facts: Seq[NetworkFact],
  nodes: Seq[NetworkInfoNodeDetail],
  routes: Seq[NetworkInfoRouteDetail],
  extraNodeIds: Seq[Long],
  extraWayIds: Seq[Long],
  extraRelationIds: Seq[Long]
) extends WithId
