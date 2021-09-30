package kpn.database.actions.routes

import kpn.api.common.route.RouteMapInfo
import kpn.database.base.Database
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

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
        project(
          fields(
            excludeId(),
            computed("routeId", "$_id"),
            computed("routeName", "$summary.name"),
            computed("networkType", "$summary.networkType"),
            computed("map", "$analysis.map"),
          )
        )
      )
      val routeMapInfo = database.routes.optionAggregate[RouteMapInfo](pipeline, log)
      (s"route map info: ${routeMapInfo.size}", routeMapInfo)
    }
  }
}
