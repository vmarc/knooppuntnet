package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
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
          networkType = data.summary.networkType,
          summary = data.summary,
          routes = data.routes.map { route =>
            val symbol = route.tags("osmc:symbol")
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
    database.networkInfos.optionAggregate[NetworkRoutesPageData](pipeline, log)
  }
}
