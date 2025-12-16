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
import kpn.database.actions.facts.MongoQueryNetworkNodes.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.repository.NetworkElement

object MongoQueryNetworkNodes {
  private val log = Log(classOf[MongoQueryNetworkNodes])
}

class MongoQueryNetworkNodes(database: Database) {
  def execute(subset: Subset, nodeIds: Seq[Long]): Seq[NetworkElement] = {
    log.debugElapsed {
      val pipeline = buildPipeline(subset, nodeIds)
      val references = database.networks.aggregate(pipeline, classOf[NetworkElement], log)
      (s"node network references: ${references.size}", references)
    }
  }

  private def buildPipeline(subset: Subset, nodeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("country", subset.country.entryName),
          equal("base.summary.routeType", subset.routeType.entryName)
        )
      ),
      unwind("$nodes"),
      filter(in("nodes.id", nodeIds *)),
      project(
        fields(
          excludeId(),
          computed("networkId", "$_id"),
          computed("networkName", "$base.summary.name"),
          computed("elementId", "$nodes.id"),
        )
      )
    )
  }
}
