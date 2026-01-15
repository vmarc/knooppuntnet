package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.network.NetworkAttributes
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.actions.networks.MongoQuerySubsetNetworks.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetNetworks {
  private val log = Log(classOf[MongoQuerySubsetNetworks])
}

class MongoQuerySubsetNetworks(database: Database) {

  def execute(subset: Subset): Seq[NetworkAttributes] = {
    val pipeline = buildPipeline(subset)
    log.debugElapsed {
      val networks = database.networks.aggregate(pipeline, classOf[NetworkAttributes], log)
      val result = s"subset ${subset.name} networks: ${networks.size}"
      (result, networks)
    }
  }

  private def buildPipeline(subset: Subset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.entryName),
          equal("base.routeType", subset.routeType.entryName)
        )
      ),
      sort(orderBy(ascending("base.name"))),
      project(
        fields(
          computed("id", "$_id"),
          include("country"),
          computed("routeType", "$base.routeType"),
          computed("routeScope", "$base.routeScope"),
          computed("name", "$base.name"),
          computed("km", "$detail.km"),
          computed("meters", "$detail.meters"),
          include("nodeCount"),
          include("routeCount"),
          computed("brokenRouteCount", "$detail.brokenRouteCount"),
          computed("brokenRoutePercentage", "$detail.brokenRoutePercentage"),
          computed("integrity", "$detail.integrity"),
          computed("inaccessibleRouteCount", "$detail.inaccessibleRouteCount"),
          computed("connectionCount", "$detail.connectionCount"),
          computed("lastUpdated", "$detail.lastUpdated"),
          computed("relationLastUpdated", "$detail.relationLastUpdated"),
          computed(" center", "$detail.center"),
        )
      )
    )
  }
}
