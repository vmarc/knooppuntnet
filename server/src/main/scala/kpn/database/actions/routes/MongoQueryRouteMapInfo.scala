package kpn.database.actions.routes

import kpn.api.common.route.RouteMapInfo
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteMapInfo {
  private val log = Log(classOf[MongoQueryRouteMapInfo])
}

class MongoQueryRouteMapInfo(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteMapInfo.log): Option[RouteMapInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          equal("_id", routeId)
        ),
        unwind("$summary.routeTypes"),
        project(
          fields(
            excludeId(),
            computed("routeId", "$_id"),
            computed("routeName", "$summary.name"),
            computed("routeType", "$summary.routeTypes"),
            include("segments"),
            include("paths"),
          )
        )
      )
      val routeMapInfo = database.routes.optionAggregate[RouteMapInfo](pipeline, log)
      (s"route map info", routeMapInfo)
    }
  }
}
