package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkDetailsPageData.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.api.analysis.pages.network.NetworkDetailsPageData

object MongoQueryNetworkDetailsPageData {
  private val log = Log(classOf[MongoQueryNetworkDetailsPageData])
}

class MongoQueryNetworkDetailsPageData(database: Database) {

  def execute(networkId: Long): Option[NetworkDetailsPageData] = {
    log.infoElapsed {
      val pipeline = buildPipeline(networkId)
      val network = database.networks.optionAggregate(pipeline, classOf[NetworkDetailsPageData], log)
      (s"network details $networkId", network)
    }
  }

  private def buildPipeline(networkId: Long): MongoPipeline = {
    Seq(
      filter(
        equal("_id", networkId)
      ),
      NetworkPipeline.lookupChangeCount(database, networkId),
      project(
        NetworkPipeline.fields(
          excludeId(),
          include("active"),
          include("country"),
          include("detail"),
          include("networkNodeIds"),
          include("connectionNodeIds"),
          include("networkRouteIds"),
          include("connectionRouteIds"),
          computed("tags", "$base.raw.tags"),
        )
      )
    )
  }
}
