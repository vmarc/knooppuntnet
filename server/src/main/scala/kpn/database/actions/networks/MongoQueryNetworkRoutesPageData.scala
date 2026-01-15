package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkRoutesPageData.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.api.analysis.pages.network.NetworkRoutesPageData

object MongoQueryNetworkRoutesPageData {
  private val log = Log(classOf[MongoQueryNetworkRoutesPageData])
}

class MongoQueryNetworkRoutesPageData(database: Database) {

  def execute(networkId: Long): Option[NetworkRoutesPageData] = {
    log.infoElapsed {
      val pipeline = buildPipeline(networkId)
      val network = database.networks.optionAggregate(pipeline, classOf[NetworkRoutesPageData], log)
      (s"network routes $networkId", network)
    }
  }

  private def buildPipeline(networkId: Long): MongoPipeline = {
    Seq(
      filter(
        equal("_id", networkId)
      ),
      project(
        fields(
          excludeId(),
          computed("summary.name", "$base.name"),
          computed("summary.routeType", "$base.routeType"),
          computed("summary.routeScope", "$base.routeScope"),
          computed("summary.factCount", "$factCount"),
          computed("summary.nodeCount", "$nodeCount"),
          computed("summary.routeCount", "$routeCount"),
          include("routes")
        )
      )
    )
  }
}
