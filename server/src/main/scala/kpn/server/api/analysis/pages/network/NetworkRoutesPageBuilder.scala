package kpn.server.api.analysis.pages.network

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.core.util.Log
import kpn.core.util.RouteSymbol
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import org.springframework.stereotype.Component

@Component
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
          routes = data.routes.map { route =>
            val symbol = RouteSymbol.from(route)
            NetworkRouteRow(
              route.id,
              route.name,
              route.length,
              route.role,
              route.investigate,
              route.accessible,
              route.roleConnection,
              route.lastUpdated,
              route.lastSurvey,
              route.proposed,
              symbol
            )
          }
        )
      }
    }
  }

  private def query(networkId: Long): Option[NetworkRoutesPageData] = {
    val pipeline = Seq(
      filter(
        equal("_id", networkId)
      ),
      project(
        fields(
          include("summary"),
          include("routes")
        )
      )
    )
    database.networks.optionAggregate(pipeline, classOf[NetworkRoutesPageData], log)
  }
}
