package kpn.server.api.analysis.pages.subset

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.util.Formatter.percentage
import kpn.core.util.Log
import kpn.server.repository.SubsetRepository
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy
import org.springframework.stereotype.Component

@Component
class SubsetNetworksPageBuilder(
  database: Database,
  subsetRepository: SubsetRepository
) {

  private val log = Log(classOf[SubsetNetworksPageBuilder])

  def build(subset: Subset): SubsetNetworksPage = {
    buildPage(subset)
  }

  private def buildPage(subset: Subset): SubsetNetworksPage = {

    val subsetInfo = subsetRepository.subsetInfo(subset)

    val networks = queryNetworks(subset)
    val routeCount = networks.map(_.summary.routeCount).sum
    val brokenRouteNetworkCount = networks.count(_.detail.brokenRouteCount > 0)
    val brokenRouteNetworkPercentage = percentage(brokenRouteNetworkCount, networks.size)
    val brokenRouteCount = networks.map(_.detail.brokenRouteCount).sum
    val brokenRoutePercentage = percentage(brokenRouteCount, routeCount)

    val xx = networks.map { networkInfoDoc =>
      NetworkAttributes(
        networkInfoDoc._id,
        networkInfoDoc.country,
        networkInfoDoc.summary.networkType,
        networkInfoDoc.summary.networkScope,
        networkInfoDoc.summary.name,
        networkInfoDoc.detail.km,
        networkInfoDoc.detail.meters,
        networkInfoDoc.summary.nodeCount,
        networkInfoDoc.summary.routeCount,
        networkInfoDoc.detail.brokenRouteCount,
        networkInfoDoc.detail.brokenRoutePercentage,
        networkInfoDoc.detail.integrity,
        networkInfoDoc.detail.unaccessibleRouteCount,
        networkInfoDoc.detail.connectionCount,
        networkInfoDoc.detail.lastUpdated,
        networkInfoDoc.detail.relationLastUpdated,
        networkInfoDoc.detail.center
      )
    }

    SubsetNetworksPage(
      subsetInfo,
      km = networks.map(_.detail.meters).sum / 1000,
      networkCount = networks.size,
      nodeCount = networks.map(_.summary.nodeCount).sum,
      routeCount = routeCount,
      brokenRouteNetworkCount = brokenRouteNetworkCount,
      brokenRouteNetworkPercentage = brokenRouteNetworkPercentage,
      brokenRouteCount = brokenRouteCount,
      brokenRoutePercentage = brokenRoutePercentage,
      unaccessibleRouteCount = networks.map(_.detail.unaccessibleRouteCount).sum,
      analysisUpdatedTime = "TODO",
      networks = xx
    )
  }

  private def queryNetworks(subset: Subset): Seq[NetworkInfoDoc] = {

    val pipeline = Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.domain),
          equal("summary.networkType", subset.networkType.name)
        )
      ),
      sort(orderBy(ascending("summary.name"))),
      project(
        fields(
          include("country"),
          include("summary"),
          include("detail"),
        )
      )
    )

    log.debugElapsed {
      val networks = database.networkInfos.aggregate[NetworkInfoDoc](pipeline, log)
      val result = s"subset ${subset.name} networks: ${networks.size}"
      (result, networks)
    }
  }
}
