package kpn.database.actions.routes

import kpn.api.common.route.RouteInfo
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.Document
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

object MongoQueryRouteInfo {
  private val log = Log(classOf[MongoQueryRouteInfo])
}

class MongoQueryRouteInfo(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteInfo.log): Option[RouteInfo] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val routeInfo = database.routes.optionAggregate[RouteInfo](pipeline, log)
      (s"route info: $routeId", routeInfo)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        equal("_id", routeId)
      ),
      project(
        fields(
          excludeId(),
          computed("routeId", "$_id"),
          computed("routeName", "$summary.name"),
          computed("routeTypes", "$summary.routeTypes"),
          computed("changeCount", Document("""{ $literal: 0 }""")),
          computed("segmentCount", Document("""{ $size: "$segments" }"""))
        )
      )
    )
  }
}
