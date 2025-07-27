package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.actions.routes.MongoQueryRouteSegmentCoordinates.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import kpn.server.monitor.domain.MonitorSegment
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQueryRouteSegmentCoordinates {
  private val log = Log(classOf[MongoQueryRouteSegmentCoordinates])
}

class MongoQueryRouteSegmentCoordinates(database: Database) {

  def execute(routeIds: Seq[Long]): Seq[MonitorSegment] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val segments = database.baseRoutes.aggregate[MonitorSegment](pipeline, log)
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
