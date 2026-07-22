package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.include
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkNodesPageData.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.api.analysis.pages.network.NetworkNodesPageData

object MongoQueryNetworkNodesPageData {
  private val log = Log(classOf[MongoQueryNetworkNodesPageData])
}

class MongoQueryNetworkNodesPageData(database: Database) {

  def execute(networkId: Long): Option[NetworkNodesPageData] = {
    log.infoElapsed {
      val pipeline = buildPipeline(networkId)
      val network = database.networks.optionAggregate(pipeline, classOf[NetworkNodesPageData], log)
      (s"network nodes $networkId", network)
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
          include("nodes")
        )
      )
    )
  }
}
