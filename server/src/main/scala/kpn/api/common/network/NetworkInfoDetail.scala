package kpn.api.common.network

import kpn.api.common.NetworkFacts

case class NetworkInfoDetail(
  nodes: Seq[NetworkInfoNode],
  routes: Seq[NetworkInfoRoute],
  networkFacts: NetworkFacts,
  shape: Option[NetworkShape]
)
