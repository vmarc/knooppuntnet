package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkRoutesPageData
import kpn.database.base.Database
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class NetworkRoutesPageBuilder(database: Database) {

  private val log = Log(classOf[NetworkRoutesPageBuilder])

  def build(networkId: Long): Option[NetworkRoutesPage] = {
    if (networkId == 1) {
      Some(NetworkRoutesPageExample.page)
    }
    else {
      query(networkId).map { data =>
        NetworkRoutesPage(
          timeInfo = TimeInfoBuilder.timeInfo,
          surveyDateInfo = SurveyDateInfoBuilder.dateInfo,
          routeType = data.summary.routeType,
          summary = data.summary,
          routes = data.routes.map(NetworkRouteRow.from)
        )
      }
    }
  }

  private def query(networkId: Long): Option[NetworkRoutesPageData] = {
    new MongoQueryNetworkRoutesPageData(database).execute(networkId)
  }
}
