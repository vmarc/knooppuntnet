package kpn.api.common.subset

import kpn.api.common.network.NetworkAttributes

case class SubsetNetworksPage(
  subsetInfo: SubsetInfo,
  km: String,
  networkCount: Long,
  nodeCount: Long,
  routeCount: Long,
  brokenRouteNetworkCount: Long,
  brokenRouteNetworkPercentage: String,
  brokenRouteCount: Long,
  brokenRoutePercentage: String,
  unaccessibleRouteCount: Long,
  analysisUpdatedTime: String,
  networks: Seq[NetworkAttributes]
)
