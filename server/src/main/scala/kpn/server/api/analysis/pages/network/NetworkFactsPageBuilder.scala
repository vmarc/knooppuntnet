package kpn.server.api.analysis.pages.network

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.network.NetworkFactsPage
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
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
    database.networks.optionAggregate(pipeline, classOf[NetworkFactsPage], log)
  }
}
