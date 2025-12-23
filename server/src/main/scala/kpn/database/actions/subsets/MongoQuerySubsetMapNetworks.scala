package kpn.database.actions.subsets

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.subset.SubsetMapNetwork
import kpn.api.custom.Subset
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetMapNetworks {
  private val log = Log(classOf[MongoQuerySubsetInfo])
}

class MongoQuerySubsetMapNetworks(database: Database) {

  def execute(subset: Subset, log: Log = MongoQuerySubsetMapNetworks.log): Seq[SubsetMapNetwork] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset)
      val subsetMapNetworks = database.networks.aggregate(pipeline, classOf[SubsetMapNetwork])
      (s"subset ${subset.name} ${subsetMapNetworks.size} networks", subsetMapNetworks)
    }
  }

  private def buildPipeline(subset: Subset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.entryName),
          equal("base.routeType", subset.routeType.entryName),
        )
      ),
      project(
        fields(
          computed("id", "$_id"),
          computed("name", "$base.name"),
          computed("km", "$detail.km"),
          include("nodeCount"),
          include("routeCount"),
          computed("center", "$detail.center"),
        )
      )
    )
  }
}
