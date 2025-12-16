package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.exclude
import com.mongodb.client.model.Projections.fields
import kpn.core.doc.ParentRouteData
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryParentRoutes.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryParentRoutes {
  private val log = Log(classOf[MongoQueryParentRoutes])
}

class MongoQueryParentRoutes(database: Database) {

  def execute(routeId: Long): Seq[ParentRouteData] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val routes = database.baseRoutes.aggregate(pipeline, classOf[ParentRouteData], log)
      (s"${routes.size} parent routes", routes)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("subRouteIds", routeId),
          equal("active", true),
        )
      ),
      project(
        fields(
          exclude("_id"),
          computed("routeId", "$_id"),
          computed("name", "$base.summary.name"),
        )
      )
    )
  }
}
