package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteSegmentCoordinates.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.server.monitor.domain.MonitorSegment

object MongoQueryRouteSegmentCoordinates {
  private val log = Log(classOf[MongoQueryRouteSegmentCoordinates])
}

class MongoQueryRouteSegmentCoordinates(database: Database) {

  def execute(routeIds: Seq[Long]): Seq[MonitorSegment] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val segments = database.baseRoutes.aggregate(pipeline, classOf[MonitorSegment], log)
      (s"${segments.size} segments", segments)
    }
  }

  private def buildPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          in("_id", routeIds: _*),
          equal("active", true),
        )
      ),
      unwind("$segmentElements"),
      project(
        fields(
          excludeId(),
          computed("relationId", "$_id"),
          computed("segmentId", "$segmentElements.segmentId"),
          computed("coordinates", "$segmentElements.coordinates"),
        )
      ),
      sort(
        orderBy(
          ascending("relationId"),
          ascending("segmentId"),
        )
      ),
    )
  }
}
