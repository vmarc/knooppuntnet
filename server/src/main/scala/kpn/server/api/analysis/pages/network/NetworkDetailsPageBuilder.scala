package kpn.server.api.analysis.pages.network

import kpn.api.common.NetworkFacts
import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkDetailsPage
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.util.Log
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkDetailsPageBuilder(
  mongoEnabled: Boolean,
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  // new
  database: Database
) {

  private val log = Log(classOf[NetworkDetailsPageBuilder])

  def build(networkId: Long): Option[NetworkDetailsPage] = {
    if (networkId == 1) {
      Some(NetworkDetailsPageExample.page)
    }
    else {
      if (mongoEnabled) {
        buildPage(networkId)
      }
      else {
        oldBuildPage(networkId)
      }
    }
  }

  private def buildPage(networkId: Long): Option[NetworkDetailsPage] = {
    database.networkInfos.findById(networkId, log).map { networkInfoDoc =>
      buildDetailsPage(networkInfoDoc)
    }
  }

  private def buildDetailsPage(networkInfoDoc: NetworkInfoDoc): NetworkDetailsPage = {
    NetworkDetailsPage(
      networkInfoDoc.summary,
      networkInfoDoc.active,
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
        center = None // TODO MONGO niet nodig op dit scherm
      ),
      networkInfoDoc.detail.tags
      // TODO MONGO networkInfoDoc.networkFacts ??
    )
  }

  // *** old couchdb based code ***

  private def oldBuildPage(networkId: Long): Option[NetworkDetailsPage] = {
    networkRepository.network(networkId).map { networkInfo =>
      val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
      NetworkDetailsPage(
        NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
        networkInfo.active,
        networkInfo.attributes,
        networkInfo.tags,
        networkInfo.detail.map(_.networkFacts).getOrElse(NetworkFacts())
      )
    }
  }
}
