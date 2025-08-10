package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.route.RouteInfo
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline
import org.bson.Document

object MongoQueryRouteInfo {
  private val log = Log(classOf[MongoQueryRouteInfo])
}

class MongoQueryRouteInfo(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteInfo.log): Option[RouteInfo] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val routeInfo = database.routes.optionAggregate(pipeline, classOf[RouteInfo], log)
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
          computed("changeCount", Document.parse("""{ $literal: 0 }""")),
          arraySize("segmentCount", "$segments")
        )
      )
    )
  }
}
