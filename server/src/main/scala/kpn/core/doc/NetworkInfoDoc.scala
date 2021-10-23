package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.NetworkFact
import kpn.api.common.data.Meta
import kpn.api.common.data.MetaData
import kpn.api.common.network.NetworkDetail
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
  nodes: Seq[NetworkInfoNodeDetail],
  routes: Seq[NetworkInfoRouteDetail],
  extraNodeIds: Seq[Long],
  extraWayIds: Seq[Long],
  extraRelationIds: Seq[Long]
) extends WithId {
  def scopedNetworkType: ScopedNetworkType = {
    ScopedNetworkType(summary.networkScope, summary.networkType)
  }
}
