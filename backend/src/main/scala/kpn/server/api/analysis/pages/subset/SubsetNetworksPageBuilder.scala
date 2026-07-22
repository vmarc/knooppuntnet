package kpn.server.api.analysis.pages.subset

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.custom.Subset
import kpn.core.util.Formatter.percentage
import kpn.core.util.Log
import kpn.server.repository.NetworkRepository
import kpn.server.repository.SubsetRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class SubsetNetworksPageBuilder(
  subsetRepository: SubsetRepository,
  networkRepository: NetworkRepository
) {

  private val log = Log(classOf[SubsetNetworksPageBuilder])

  def build(subset: Subset): SubsetNetworksPage = {

    val subsetInfo = subsetRepository.subsetInfo(subset)

    val networkAttributess = queryNetworks(subset)
    val routeCount = networkAttributess.map(_.routeCount).sum
    val brokenRouteNetworkCount = networkAttributess.count(_.brokenRouteCount > 0)
    val brokenRouteNetworkPercentage = percentage(brokenRouteNetworkCount, networkAttributess.size)
    val brokenRouteCount = networkAttributess.map(_.brokenRouteCount).sum
    val brokenRoutePercentage = percentage(brokenRouteCount, routeCount)

    val bounds = Option.when(networkAttributess.nonEmpty) {
      Bounds.from(networkAttributess.flatMap(_.center))
    }

    SubsetNetworksPage(
      subsetInfo,
      km = networkAttributess.map(_.meters).sum / 1000,
      networkCount = networkAttributess.size,
      nodeCount = networkAttributess.map(_.nodeCount).sum,
      routeCount = routeCount,
      brokenRouteNetworkCount = brokenRouteNetworkCount,
      brokenRouteNetworkPercentage = brokenRouteNetworkPercentage,
      brokenRouteCount = brokenRouteCount,
      brokenRoutePercentage = brokenRoutePercentage,
      inaccessibleRouteCount = networkAttributess.map(_.inaccessibleRouteCount).sum,
      analysisUpdatedTime = "TODO",
      bounds,
      networks = networkAttributess
    )
  }

  private def queryNetworks(subset: Subset): Seq[NetworkAttributes] = {
    networkRepository.subsetNetworks(subset)
  }
}
