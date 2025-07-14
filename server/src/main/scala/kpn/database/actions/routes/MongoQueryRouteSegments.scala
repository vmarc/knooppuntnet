package kpn.database.actions.routes

import kpn.core.doc.SuperSubSegmentInfo
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryRouteSegments {
  private val log = Log(classOf[MongoQueryRouteSegments])
}

class MongoQueryRouteSegments(database: Database) {

  def execute(routeIds: Seq[Long], log: Log = MongoQueryRouteSegments.log): Seq[SuperSubSegmentInfo] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeIds)
      val elements = database.baseRoutes.aggregate[SuperSubSegmentInfo](pipeline, log)
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
          computed("id", 0),
          computed("relationId", "_id"),
          computed("segmentId", "id"),
          include("startNodeId"),
          include("endNodeId"),
          include("meters"),
          include("bounds"),
        )
      )
    )
  }
}
