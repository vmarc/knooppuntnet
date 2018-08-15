package kpn.shared.network

import kpn.shared.NetworkFacts

case class NetworkInfoDetail(
  nodes: Seq[NetworkNodeInfo2] = Seq(),
  routes: Seq[NetworkRouteInfo] = Seq(),
  networkFacts: NetworkFacts = NetworkFacts(),
  shape: Option[NetworkShape]
)
