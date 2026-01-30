package kpn.api.common.subset

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkAttributes

case class SubsetNetworksPage(
  subsetInfo: SubsetInfo,
  km: Long,
  networkCount: Long,
  nodeCount: Long,
  routeCount: Long,
  brokenRouteNetworkCount: Long,
  brokenRouteNetworkPercentage: String,
  brokenRouteCount: Long,
  brokenRoutePercentage: String,
  inaccessibleRouteCount: Long,
  analysisUpdatedTime: String,
  bounds: Option[Bounds],
  networks: Seq[NetworkAttributes]
)
