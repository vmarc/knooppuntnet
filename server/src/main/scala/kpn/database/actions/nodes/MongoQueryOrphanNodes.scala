package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.OrphanNodeInfo
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryOrphanNodes.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.arrayEmpty
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline

object MongoQueryOrphanNodes {
  private val log = Log(classOf[MongoQueryOrphanNodes])
}

class MongoQueryOrphanNodes(database: Database) {

  def execute(): Seq[OrphanNodeInfo] = {
    val pipeline = buildPipeline()
    log.debugElapsed {
      val docs = database.nodes.aggregate(pipeline, classOf[OrphanNodeInfo], log)
      val message = s"orphan nodes: ${docs.size}"
      (message, docs)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          arrayEmpty("routeReferences"),
          arrayEmpty("networkRelationReferences"),
        )
      ),
      unwind("$base.names"),
      sort(
        orderBy(
          ascending(
            "base.names.name"
          )
        )
      ),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$base.names.name"),
          computed("longName", "$base.names.longName"),
          computed("proposed", "$base.names.proposed"),
          computed("lastUpdated", "$base.lastUpdated"),
          computed("lastSurvey", "$base.lastSurvey"),
          arraySize("factCount", "$facts"),
        )
      )
    )
  }
}
