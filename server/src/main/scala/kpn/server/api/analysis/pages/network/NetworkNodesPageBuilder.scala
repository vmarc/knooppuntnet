package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkNodesPage
import kpn.core.mongo.Database
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
class NetworkNodesPageBuilder(database: Database) {

  private val log = Log(classOf[NetworkNodesPageBuilder])

  def build(networkId: Long): Option[NetworkNodesPage] = {
    if (networkId == 1) {
      Some(NetworkNodesPageExample.nodesPage)
    }
    else {
      mongoBuildPage(networkId)
    }
  }

  private def mongoBuildPage(networkId: Long): Option[NetworkNodesPage] = {
    val pipeline = Seq(
      filter(
        equal("_id", networkId)
      ),
      project(
        fields(
          include("summary"),
          include("nodes")
        )
      )
    )
    database.networkInfos.optionAggregate[NetworkNodesPage](pipeline, log).map { page =>
      page.copy(
        timeInfo = TimeInfoBuilder.timeInfo,
        surveyDateInfo = SurveyDateInfoBuilder.dateInfo,
        networkType = page.summary.networkType, // TODO MONGO double?
        networkScope = page.summary.networkScope, // TODO MONGO double?
      )
    }
  }
}
