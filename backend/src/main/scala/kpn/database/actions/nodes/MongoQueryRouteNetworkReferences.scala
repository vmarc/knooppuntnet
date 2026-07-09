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

object MongoQueryRouteNetworkReferences {
  private val log = Log(classOf[MongoQueryRouteNetworkReferences])
}

class MongoQueryRouteNetworkReferences(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteNetworkReferences.log): Seq[Reference] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val references = database.baseNetworks.aggregate(pipeline, classOf[Reference], log)
      (s"route network references: ${references.size}", references)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("routeIds", routeId)
        ),
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
