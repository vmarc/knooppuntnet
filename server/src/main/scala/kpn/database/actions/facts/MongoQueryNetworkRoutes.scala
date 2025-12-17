package kpn.database.actions.facts

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.facts.MongoQueryNetworkRoutes.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.repository.NetworkElement

object MongoQueryNetworkRoutes {
  private val log = Log(classOf[MongoQueryNetworkRoutes])
}

class MongoQueryNetworkRoutes(database: Database) {

  def execute(subset: Subset, routeIds: Seq[Long]): Seq[NetworkElement] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, routeIds)
      val references = database.networks.aggregate(pipeline, classOf[NetworkElement], log)
      (s"route network references: ${references.size}", references)
    }
  }

  private def buildPipeline(subset: Subset, routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.entryName),
          equal("base.routeType", subset.routeType.entryName)
        )
      ),
      unwind("$routes"),
      filter(in("routes.id", routeIds *)),
      project(
        fields(
          excludeId(),
          computed("networkId", "$_id"),
          computed("networkName", "$base.name"),
          computed("elementId", "$routes.id"),
        )
      )
    )
  }
}
