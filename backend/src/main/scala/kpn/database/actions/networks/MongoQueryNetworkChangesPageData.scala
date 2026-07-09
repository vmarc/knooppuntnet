package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.excludeId
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQueryNetworkChangesPageData.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.api.analysis.pages.network.NetworkChangesPageData

object MongoQueryNetworkChangesPageData {
  private val log = Log(classOf[MongoQueryNetworkChangesPageData])
}

class MongoQueryNetworkChangesPageData(database: Database) {

  def execute(networkId: Long): Option[NetworkChangesPageData] = {
    log.infoElapsed {
      val pipeline = buildPipeline(networkId)
      val network = database.networks.optionAggregate(pipeline, classOf[NetworkChangesPageData], log)
      (s"network changes $networkId", network)
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
        )
      )
    )
  }
}
