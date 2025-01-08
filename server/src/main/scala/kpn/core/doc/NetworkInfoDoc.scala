package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.Country
import kpn.api.common.NetworkFact
import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.ScopedRouteType

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
  def scopedRouteType: ScopedRouteType = {
    ScopedRouteType(summary.networkScope, summary.routeType)
  }
}
