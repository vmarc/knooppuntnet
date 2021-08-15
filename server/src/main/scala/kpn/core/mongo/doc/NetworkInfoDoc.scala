package kpn.core.mongo.doc

import kpn.api.base.WithId
import kpn.api.common.NetworkFact
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkNodeDetail
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Country
import kpn.api.custom.ScopedNetworkType

case class NetworkInfoDoc(
  _id: Long,
  active: Boolean,
  country: Option[Country],
  summary: NetworkSummary,
  detail: NetworkDetail,
  facts: Seq[NetworkFact],
  nodes: Seq[NetworkNodeDetail],
  routes: Seq[NetworkRouteRow], // TODO MONGO use better name
  extraNodeIds: Seq[Long],
  extraWayIds: Seq[Long],
  extraRelationIds: Seq[Long],
  nodeIds: Seq[Long] // contains both ids of nodes in network relation and route relations and ways // TODO MONGO still needed after NetworkUpdater implementation?
) extends WithId {

  def scopedNetworkType: ScopedNetworkType = {
    ScopedNetworkType(summary.networkScope, summary.networkType)
  }
}
