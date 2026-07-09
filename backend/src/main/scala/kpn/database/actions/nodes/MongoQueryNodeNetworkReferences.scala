package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryNodeNetworkReferences {
  private val log = Log(classOf[MongoQueryNodeBaseNetworkReferences])
}

class MongoQueryNodeNetworkReferences(database: Database) {

  def execute(nodeId: Long, log: Log = MongoQueryNodeNetworkReferences.log): Seq[Reference] = {
    val pipeline = buildPipeline(nodeId)
    log.debugElapsed {
      val references = database.networks.aggregate(pipeline, classOf[Reference], log)
      (s"node network references: ${references.size}", references)
    }
  }

  private def buildPipeline(nodeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("nodes.id", nodeId),
        )
      ),
      project(
        fields(
          excludeId(),
          computed("routeType", "$base.routeType"),
          computed("routeScope", "$base.routeScope"),
          computed("id", "$_id"),
          computed("name", "$base.name"),
        )
      )
    )
  }
}
