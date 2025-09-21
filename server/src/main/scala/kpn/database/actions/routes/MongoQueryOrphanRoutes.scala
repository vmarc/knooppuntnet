package kpn.database.actions.routes

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
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.arrayEmpty
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryOrphanRoutes {
  private val log = Log(classOf[MongoQueryOrphanRoutes])
}

class MongoQueryOrphanRoutes(database: Database) {

  def execute(log: Log = MongoQueryOrphanRoutes.log): Seq[OrphanRouteInfo] = {
    val pipeline = buildPipeline()
    log.debugElapsed {
      val docs = database.routes.aggregate(pipeline, classOf[OrphanRouteInfo], log)
      val message = s"orphan routes: ${docs.size}"
      (message, docs)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("summary.nodeNetwork", true),
          arrayEmpty("networkReferences"),
        )
      ),
      sort(
        orderBy(
          ascending(
            "summary.name"
          )
        )
      ),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$summary.name"),
          computed("meters", "$summary.meters"),
          computed("isBroken", "$summary.broken"),
          computed("inaccessible", "$summary.inaccessible"),
          include("lastSurvey"),
          include("lastUpdated"),
          include("facts"),
        )
      )
    )
  }
}
