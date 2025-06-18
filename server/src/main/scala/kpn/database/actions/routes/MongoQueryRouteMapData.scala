package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.server.api.analysis.pages.route.RouteMapData
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteMapData {
  private val log = Log(classOf[MongoQueryRouteMapData])
}

class MongoQueryRouteMapData(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteMapData.log): Option[RouteMapData] = {
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
            computed("routeTypes", "$summary.routeTypes"),
            include("segments"),
            include("paths"),
          )
        )
      )
      val data = database.routes.optionAggregate[RouteMapData](pipeline, log)
      (s"route map data", data)
    }
  }
}
