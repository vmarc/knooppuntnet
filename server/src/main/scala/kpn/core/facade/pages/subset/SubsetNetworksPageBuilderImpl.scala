package kpn.core.facade.pages.subset

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.core.repository.OverviewRepository
import kpn.core.util.Formatter.number
import kpn.core.util.Formatter.percentage
import kpn.shared.Subset
import kpn.shared.subset.SubsetNetworksPage

class SubsetNetworksPageBuilderImpl(
  overviewRepository: OverviewRepository,
  networkRepository: NetworkRepository
) extends SubsetNetworksPageBuilder {

  override def build(subset: Subset): SubsetNetworksPage = {

    val figures = overviewRepository.figures(Couch.uiTimeout)
    val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
    val networks = networkRepository.networks(subset, Couch.uiTimeout)

    val routeCount = networks.map(_.routeCount).sum
    val brokenRouteNetworkCount = networks.count(_.brokenRouteCount > 0)
    val brokenRouteNetworkPercentage = percentage(brokenRouteNetworkCount, networks.size)
    val brokenRouteCount = networks.map(_.brokenRouteCount).sum
    val brokenRoutePercentage = percentage(brokenRouteCount, routeCount)

    SubsetNetworksPage(
      subsetInfo,
      km = number(networks.map(_.meters).sum / 1000),
      networkCount = networks.size,
      nodeCount = networks.map(_.nodeCount).sum,
      routeCount = routeCount,
      brokenRouteNetworkCount = brokenRouteNetworkCount,
      brokenRouteNetworkPercentage = brokenRouteNetworkPercentage,
      brokenRouteCount = networks.map(_.brokenRouteCount).sum,
      brokenRoutePercentage = brokenRoutePercentage,
      unaccessibleRouteCount = networks.map(_.unaccessibleRouteCount).sum,
      analysisUpdatedTime = "TODO",
      networks = networks
    )
  }
}
