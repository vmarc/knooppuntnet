package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkRoutesPage
import kpn.database.base.Database
import kpn.core.util.Log
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
      mongoBuildPage(networkId)
    }
  }

  private def mongoBuildPage(networkId: Long): Option[NetworkRoutesPage] = {
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
    database.networkInfos.optionAggregate[NetworkRoutesPage](pipeline, log).map { page =>
      page.copy(
        timeInfo = TimeInfoBuilder.timeInfo,
        surveyDateInfo = SurveyDateInfoBuilder.dateInfo,
      )
    }
  }

}
