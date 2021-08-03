package kpn.core.mongo.doc

import kpn.api.base.WithId
import kpn.api.common.NetworkFacts
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class NetworkDoc(
  _id: Long,
  active: Boolean,
  relationLastUpdated: Timestamp,
  nodeMembers: Seq[NetworkNodeMember],
  wayMembers: Seq[NetworkWayMember],
  relationMembers: Seq[NetworkRelationMember],
  networkFacts: NetworkFacts, // TODO MONGO eliminate networkFacts from this class
  tags: Tags
) extends WithId
