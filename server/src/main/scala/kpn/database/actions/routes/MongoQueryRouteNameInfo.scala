package kpn.database.actions.routes

import kpn.api.common.route.RouteNameInfo
import kpn.database.base.Database
import kpn.core.doc.Label
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

object MongoQueryRouteNameInfo {
  private val log = Log(classOf[MongoQueryRouteNameInfo])
}

class MongoQueryRouteNameInfo(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteNameInfo.log): Option[RouteNameInfo] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("_id", routeId),
            equal("labels", Label.active)
          )
        ),
        project(
          fields(
            excludeId(),
            computed("routeId", "$_id"),
            computed("routeName", "$summary.name"),
            computed("networkType", "$summary.networkType")
          )
        )
      )
      val routeNameInfo = database.routes.optionAggregate[RouteNameInfo](pipeline, log)
      (s"route name info: ${routeNameInfo.size}", routeNameInfo)
    }
  }
}
