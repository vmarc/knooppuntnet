package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkFactsPage
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkFactsPageData
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class NetworkFactsPageBuilder(database: Database) {

  private val log = Log(classOf[NetworkFactsPageBuilder])

  def build(networkId: Long): Option[NetworkFactsPage] = {
    if (networkId == 1) {
      Some(NetworkFactsPageExample.page)
    }
    else {
      buildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkFactsPage] = {
    query(networkId).map { data =>
      NetworkFactsPage(
        data.summary,
        data.facts
      )
    }
  }

  private def query(networkId: Long): Option[NetworkFactsPageData] = {
    new MongoQueryNetworkFactsPageData(database).execute(networkId)
  }
}
