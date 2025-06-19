package kpn.database.actions.routes

import kpn.api.common.route.RoutePathData
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRoutePathData {
  private val log = Log(classOf[MongoQueryRoutePathData])
}

class MongoQueryRoutePathData(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRoutePathData.log): Option[RoutePathData] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("_id", routeId)
        ),
        project(
          fields(
            excludeId(),
            computed("name", "$summary.name"),
            computed("routeTypes", "$summary.routeTypes"),
            include("paths"),
            include("bounds")
          )
        )
      )
      val data = database.routes.optionAggregate[RoutePathData](pipeline, log)
      (s"route path data: ${data.size}", data)
    }
  }
}
