package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkDetailsPage
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class NetworkDetailsPageBuilder(database: Database) {

  private val log = Log(classOf[NetworkDetailsPageBuilder])

  def build(networkId: Long): Option[NetworkDetailsPage] = {
    if (networkId == 1) {
      Some(NetworkDetailsPageExample.page)
    }
    else {
      buildPage(networkId)
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
}
