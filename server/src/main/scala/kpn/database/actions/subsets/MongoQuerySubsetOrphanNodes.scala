package kpn.database.actions.subsets

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
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.arrayEmpty
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetOrphanNodes {
  private val log = Log(classOf[MongoQuerySubsetOrphanNodes])
}

class MongoQuerySubsetOrphanNodes(database: Database) {

  def execute(subset: Subset): Seq[OrphanNodeInfo] = {
    val pipeline = buildPipeline(subset)
    log.debugElapsed {
      val docs = database.nodes.aggregate(pipeline, classOf[OrphanNodeInfo], log).distinct
      val message = s"subset ${subset.name} orphan nodes: ${docs.size}"
      (message, docs)
    }
  }

  private def buildPipeline(subset: Subset): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("labels", Label.country(subset.country)),
          equal("labels", Label.routeType(subset.routeType)),
          arrayEmpty("routeReferences"),
          arrayEmpty("networkRelationReferences"),
        )
      ),
      unwind("$base.names"),
      filter(
        equal("base.names.routeType", subset.routeType.entryName),
      ),
      sort(
        orderBy(
          ascending(
            "name"
          )
        )
      ),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$base.name"),
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
