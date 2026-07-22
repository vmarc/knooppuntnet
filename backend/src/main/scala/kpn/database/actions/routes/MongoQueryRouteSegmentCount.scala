package kpn.database.actions.routes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.core.doc.Storable
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline

case class SegmentCountDoc(
  segmentCount: Long,
) extends Storable

object MongoQueryRouteSegmentCount {
  private val log = Log(classOf[MongoQueryRouteSegmentCount])
}

class MongoQueryRouteSegmentCount(database: Database) {

  def execute(routeId: Long, log: Log = MongoQueryRouteSegmentCount.log): Option[Long] = {
    log.debugElapsed {
      val pipeline = buildPipeline(routeId)
      val segmentCount = database.baseRoutes.optionAggregate(pipeline, classOf[SegmentCountDoc], log).map(_.segmentCount)
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
