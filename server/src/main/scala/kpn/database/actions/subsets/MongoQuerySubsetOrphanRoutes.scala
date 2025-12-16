package kpn.database.actions.subsets

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.arrayEmpty
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubsetOrphanRoutes {
  private val log = Log(classOf[MongoQuerySubsetOrphanRoutes])
}

class MongoQuerySubsetOrphanRoutes(database: Database) {

  def execute(subset: Subset, log: Log = MongoQuerySubsetOrphanRoutes.log): Seq[OrphanRouteInfo] = {
    val pipeline = buildPipeline(subset)
    log.debugElapsed {
      val docs = database.routes.aggregate(pipeline, classOf[OrphanRouteInfo], log)
      val message = s"subset ${subset.name} orphan routes: ${docs.size}"
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
          equal("base.summary.nodeNetwork", true),
          arrayEmpty("networkReferences"),
        )
      ),
      sort(
        orderBy(
          ascending(
            "base.summary.name"
          )
        )
      ),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$base.summary.name"),
          computed("meters", "$base.summary.meters"),
          computed("isBroken", "$base.summary.broken"),
          computed("lastSurvey", "$base.lastSurvey"),
          computed("lastUpdated", "$base.lastUpdated"),
          include("facts"),
        )
      )
    )
  }
}
