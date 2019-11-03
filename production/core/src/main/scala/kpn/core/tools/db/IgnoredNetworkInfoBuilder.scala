package kpn.core.tools.db

import kpn.core.engine.analysis.NetworkRelationAnalysis
import kpn.core.load.data.LoadedNetwork
import kpn.shared.Fact
import kpn.shared.network.Integrity
import kpn.shared.network.NetworkAttributes
import kpn.shared.network.NetworkInfo

object IgnoredNetworkInfoBuilder {

  def build(networkRelationAnalysis: NetworkRelationAnalysis, loadedNetwork: LoadedNetwork, ignoreReasons: Seq[Fact]): NetworkInfo = {

    val attributes = NetworkAttributes(
      id = loadedNetwork.networkId,
      country = networkRelationAnalysis.country,
      networkType = loadedNetwork.networkType,
      name = loadedNetwork.name,
      km = 0,
      meters = 0,
      nodeCount = 0,
      routeCount = 0,
      brokenRouteCount = 0,
      brokenRoutePercentage = "",
      integrity = Integrity(),
      unaccessibleRouteCount = 0,
      connectionCount = 0,
      lastUpdated = networkRelationAnalysis.lastUpdated,
      relationLastUpdated = loadedNetwork.relation.timestamp,
      None
    )

    NetworkInfo(
      attributes,
      active = true,
      ignored = true,
      nodeRefs = networkRelationAnalysis.nodeRefs,
      routeRefs = networkRelationAnalysis.routeRefs,
      networkRefs = networkRelationAnalysis.networkRefs,
      facts = ignoreReasons,
      tags = loadedNetwork.relation.tags,
      detail = None
    )
  }
}
