package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkDetailsPage
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.database.base.Database
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
    database.networks.findById(networkId, log).map { networkDoc =>
      buildDetailsPage(networkDoc)
    }
  }

  private def buildDetailsPage(networkDoc: NetworkDoc): NetworkDetailsPage = {
    NetworkDetailsPage(
      networkDoc.summary,
      networkDoc.active,
      NetworkAttributes(
        networkDoc._id,
        networkDoc.country,
        networkDoc.base.routeType,
        networkDoc.base.routeScope,
        networkDoc.base.name,
        networkDoc.detail.km,
        networkDoc.detail.meters,
        networkDoc.nodeCount,
        networkDoc.routeCount,
        networkDoc.detail.brokenRouteCount,
        networkDoc.detail.brokenRoutePercentage,
        networkDoc.detail.integrity,
        networkDoc.detail.inaccessibleRouteCount,
        networkDoc.detail.connectionCount,
        networkDoc.detail.lastUpdated,
        networkDoc.detail.relationLastUpdated,
        center = None // TODO MONGO niet nodig op dit scherm
      ),
      networkDoc.tags
      // TODO MONGO networkInfoDoc.networkFacts ??
    )
  }
}
