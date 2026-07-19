package kpn.api.common.subset;

import kpn.api.common.Bounds;
import kpn.api.common.network.NetworkAttributes;
import kpn.api.common.subset.SubsetInfo;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record SubsetNetworksPage(
  SubsetInfo subsetInfo,
  Long km,
  Long networkCount,
  Long nodeCount,
  Long routeCount,
  Long brokenRouteNetworkCount,
  String brokenRouteNetworkPercentage,
  Long brokenRouteCount,
  String brokenRoutePercentage,
  Long inaccessibleRouteCount,
  String analysisUpdatedTime,
  Optional<Bounds> bounds,
  ImmutableList<NetworkAttributes> networks
) {
}

/*
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

*/
