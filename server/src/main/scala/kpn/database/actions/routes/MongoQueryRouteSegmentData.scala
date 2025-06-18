package kpn.database.actions.routes

import kpn.api.common.route.RouteSegmentData
import kpn.core.util.Log
import kpn.database.base.Database
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteSegmentData {
  private val log = Log(classOf[MongoQueryRouteSegmentData])
}

class MongoQueryRouteSegmentData(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteSegmentData.log): Option[RouteSegmentData] = {
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
            include("segments"),
            include("bounds")
          )
        )
      )
      val data = database.routes.optionAggregate[RouteSegmentData](pipeline, log)
      (s"route name info: ${data.size}", data)
    }
  }
}
