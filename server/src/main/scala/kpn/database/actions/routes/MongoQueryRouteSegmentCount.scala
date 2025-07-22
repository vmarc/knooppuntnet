package kpn.database.actions.routes

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

case class SegmentCountDoc(
  segmentCount: Long,
)

object MongoQueryRouteSegmentCount {
  private val log = Log(classOf[MongoQueryRouteSegmentCount])
}

class MongoQueryRouteSegmentCount(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteSegmentCount.log): Option[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val segmentCount = database.baseRoutes.optionAggregate[SegmentCountDoc](pipeline, log).map(_.segmentCount)
      (s"route segmentCount: $segmentCount", segmentCount)
    }
  }

  private def buildPipeline(routeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("_id", routeId),
        )
      ),
      project(
        fields(
          excludeId(),
          arraySize("segmentCount", "$segments")
        )
      )
    )
  }
}
