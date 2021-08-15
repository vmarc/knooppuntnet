package kpn.core.mongo.doc

import kpn.api.base.WithId
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object NetworkDoc {

  def from(rawRelation: RawRelation): NetworkDoc = {
    val nodeMembers = rawRelation.nodeMembers.map { member =>
      NetworkNodeMember(member.ref, member.role)
    }
    val wayMembers = rawRelation.wayMembers.map { member =>
      NetworkWayMember(member.ref, member.role)
    }
    val relationMembers = rawRelation.relationMembers.map { member =>
      NetworkRelationMember(member.ref, member.role)
    }
    NetworkDoc(
      rawRelation.id,
      active = true,
      rawRelation.timestamp,
      nodeMembers,
      wayMembers,
      relationMembers,
      rawRelation.tags
    )
  }
}

case class NetworkDoc(
  _id: Long,
  active: Boolean,
  relationLastUpdated: Timestamp,
  nodeMembers: Seq[NetworkNodeMember],
  wayMembers: Seq[NetworkWayMember],
  relationMembers: Seq[NetworkRelationMember],
  tags: Tags
) extends WithId
