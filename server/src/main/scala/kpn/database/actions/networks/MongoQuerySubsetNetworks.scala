package kpn.database.actions.networks

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.custom.Subset
import kpn.core.doc.NetworkDoc
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

  def execute(subset: Subset): Seq[NetworkDoc] = {
    val pipeline = buildPipeline(subset)
    log.debugElapsed {
      val networks = database.networks.aggregate(pipeline, classOf[NetworkDoc], log)
      val result = s"subset ${subset.name} networks: ${networks.size}"
      (result, networks)
    }
  }

  private def buildPipeline(subset: Subset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.toString),
          equal("summary.routeType", subset.routeType.toString)
        )
      ),
      sort(orderBy(ascending("summary.name"))),
      project(
        fields(
          include("country"),
          include("summary"),
          include("detail"),
        )
      )
    )
  }
}
