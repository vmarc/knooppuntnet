package kpn.api.common.network

import kpn.api.common.NetworkFacts

case class NetworkInfoDetail(
  nodes: Seq[NetworkNodeInfo2] = Seq.empty,
  routes: Seq[NetworkRouteInfo] = Seq.empty,
  networkFacts: NetworkFacts = NetworkFacts(),
  shape: Option[NetworkShape]
)
