package kpn.server.api.analysis.pages.subset

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.custom.Subset
import kpn.core.doc.NetworkDoc
import kpn.core.util.Formatter.percentage
import kpn.core.util.Log
import kpn.server.repository.NetworkRepository
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetNetworksPageBuilder(
  subsetRepository: SubsetRepository,
  networkRepository: NetworkRepository
) {

  private val log = Log(classOf[SubsetNetworksPageBuilder])

  def build(subset: Subset): SubsetNetworksPage = {

    val subsetInfo = subsetRepository.subsetInfo(subset)

    val networkDocs = queryNetworks(subset)
    val routeCount = networkDocs.map(_.summary.routeCount).sum
    val brokenRouteNetworkCount = networkDocs.count(_.detail.brokenRouteCount > 0)
    val brokenRouteNetworkPercentage = percentage(brokenRouteNetworkCount, networkDocs.size)
    val brokenRouteCount = networkDocs.map(_.detail.brokenRouteCount).sum
    val brokenRoutePercentage = percentage(brokenRouteCount, routeCount)

    val networks = networkDocs.map(toNetworkAttributes)

    SubsetNetworksPage(
      subsetInfo,
      km = networkDocs.map(_.detail.meters).sum / 1000,
      networkCount = networkDocs.size,
      nodeCount = networkDocs.map(_.summary.nodeCount).sum,
      routeCount = routeCount,
      brokenRouteNetworkCount = brokenRouteNetworkCount,
      brokenRouteNetworkPercentage = brokenRouteNetworkPercentage,
      brokenRouteCount = brokenRouteCount,
      brokenRoutePercentage = brokenRoutePercentage,
      inaccessibleRouteCount = networkDocs.map(_.detail.inaccessibleRouteCount).sum,
      analysisUpdatedTime = "TODO",
      networks = networks
    )
  }

  private def queryNetworks(subset: Subset): Seq[NetworkDoc] = {
    networkRepository.subsetNetworks(subset)
  }

  private def toNetworkAttributes(networkDoc: NetworkDoc): NetworkAttributes = {
    NetworkAttributes(
      networkDoc._id,
      networkDoc.country,
      networkDoc.summary.routeType,
      networkDoc.summary.routeScope,
      networkDoc.summary.name,
      networkDoc.detail.km,
      networkDoc.detail.meters,
      networkDoc.summary.nodeCount,
      networkDoc.summary.routeCount,
      networkDoc.detail.brokenRouteCount,
      networkDoc.detail.brokenRoutePercentage,
      networkDoc.detail.integrity,
      networkDoc.detail.inaccessibleRouteCount,
      networkDoc.detail.connectionCount,
      networkDoc.detail.lastUpdated,
      networkDoc.detail.relationLastUpdated,
      networkDoc.detail.center
    )
  }
}
