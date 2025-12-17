package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.core.doc.SubRouteData
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQuerySubRouteData.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQuerySubRouteData {
  private val log = Log(classOf[MongoQuerySubRouteData])
}

class MongoQuerySubRouteData(database: Database) {

  def execute(routeId: Long): Option[SubRouteData] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val routes = database.baseRoutes.optionAggregate(pipeline, classOf[SubRouteData], log)
      (s"${routes.size} routes", routes)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("_id", routeId),
          equal("active", true),
        )
      ),
      project(
        fields(
          include("_id"),
          computed("name", "$base.name"),
          computed("members", "$base.members"),
          computed("distance", "$base.meters"),
          include("segments"),
        )
      )
    )
  }
}
