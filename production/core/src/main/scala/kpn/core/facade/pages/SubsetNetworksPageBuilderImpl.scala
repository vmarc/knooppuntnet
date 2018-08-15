package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.core.repository.OverviewRepository
import kpn.core.util.Formatter.number
import kpn.core.util.Formatter.percentage
import kpn.shared.LatLonImpl
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

    // bring network "Brabantse Wal" to the front of the list
    val sortedNetworks = networks.filter(_.id == 3138543) ++ networks.filterNot(_.id == 3138543)

    val routeCount = networks.map(_.routeCount).sum
    val brokenRouteNetworkCount = networks.count(_.brokenRouteCount > 0)
    val brokenRouteNetworkPercentage = percentage(brokenRouteNetworkCount, networks.size)
    val brokenRouteCount = networks.map(_.brokenRouteCount).sum
    val brokenRoutePercentage = percentage(brokenRouteCount, routeCount)

    val networksWithFixedCenter = sortedNetworks.map { network => // TODO remove this temporary fix once database has been regenerated
      network.center match {
        case Some(LatLonImpl("NaN", "NaN")) => network.copy(center = None)
        case _ => network
      }
    }

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
      networks = networksWithFixedCenter
    )
  }
}
