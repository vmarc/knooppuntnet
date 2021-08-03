package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkFactsPage
import kpn.core.mongo.Database
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
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
    val pipeline = Seq(
      filter(
        equal("_id", networkId)
      ),
      project(
        fields(
          include("summary"),
          include("facts")
        )
      )
    )
    database.networkInfos.optionAggregate[NetworkFactsPage](pipeline, log)
  }
}
