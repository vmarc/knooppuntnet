package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.core.doc.SuperSubSegmentInfo
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryRouteSegments {
  private val log = Log(classOf[MongoQueryRouteSegments])
}

class MongoQueryRouteSegments(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteSegments.log): Seq[SuperSubSegmentInfo] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val elements = database.routes.aggregate(pipeline, classOf[SuperSubSegmentInfo], log)
      val updatedElements = elements.zipWithIndex.map { case (element, index) => element.copy(id = index + 1) }
      (s"${elements.size} segment elements", updatedElements)
    }
  }

  private def buildPipeline(routeIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("_id", routeIds: _*)
        )
      ),
      unwind("$segments"),
      project(
        fields(
          excludeId(),
          computed("relationId", "$_id"),
          computed("segmentId", "$segments.id"),
          computed("startNodeId", "$segments.startNodeId"),
          computed("endNodeId", "$segments.endNodeId"),
          computed("meters", "$segments.meters"),
          computed("bounds", "$segments.bounds"),
        )
      )
    )
  }
}
