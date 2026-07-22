package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkDetailsPage
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkDetailsPageData
import kpn.database.base.Database
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
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
    query(networkId).map(buildDetailsPage)
  }

  private def buildDetailsPage(data: NetworkDetailsPageData): NetworkDetailsPage = {
    NetworkDetailsPage(
      data.summary,
      data.active,
      data.country,
      data.detail,
      data.networkNodeIds,
      data.connectionNodeIds,
      data.networkRouteIds,
      data.connectionRouteIds,
      data.tags
    )
  }

  private def query(networkId: Long): Option[NetworkDetailsPageData] = {
    new MongoQueryNetworkDetailsPageData(database).execute(networkId)
  }
}
