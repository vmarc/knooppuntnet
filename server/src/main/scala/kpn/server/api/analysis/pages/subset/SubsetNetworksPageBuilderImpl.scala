package kpn.server.api.analysis.pages.subset

import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.custom.Subset
import kpn.core.util.Formatter.percentage
import kpn.server.repository.NetworkRepository
import kpn.server.repository.OverviewRepository
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetNetworksPageBuilderImpl(
  overviewRepository: OverviewRepository,
  networkRepository: NetworkRepository,
  subsetRepository: SubsetRepository,
  // old
  mongoEnabled: Boolean
) extends SubsetNetworksPageBuilder {

  override def build(subset: Subset): SubsetNetworksPage = {

    val subsetInfo = if (mongoEnabled) {
      subsetRepository.subsetInfo(subset)
    }
    else {
      val figures = overviewRepository.figures()
      SubsetInfoBuilder.newSubsetInfo(subset, figures)
    }

    val networks = networkRepository.networks(subset)

    val routeCount = networks.map(_.routeCount).sum
    val brokenRouteNetworkCount = networks.count(_.brokenRouteCount > 0)
    val brokenRouteNetworkPercentage = percentage(brokenRouteNetworkCount, networks.size)
    val brokenRouteCount = networks.map(_.brokenRouteCount).sum
    val brokenRoutePercentage = percentage(brokenRouteCount, routeCount)

    SubsetNetworksPage(
      subsetInfo,
      km = networks.map(_.meters).sum / 1000,
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
