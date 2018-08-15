package kpn.shared.subset

import kpn.shared.network.NetworkAttributes

case class SubsetNetworksPage(
  subsetInfo: SubsetInfo,
  km: String,
  networkCount: Int,
  nodeCount: Int,
  routeCount: Int,
  brokenRouteNetworkCount: Int,
  brokenRouteNetworkPercentage: String,
  brokenRouteCount: Int,
  brokenRoutePercentage: String,
  unaccessibleRouteCount: Int,
  analysisUpdatedTime: String,
  networks: Seq[NetworkAttributes]
)
