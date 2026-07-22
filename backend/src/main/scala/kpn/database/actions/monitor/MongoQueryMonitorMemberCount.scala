package kpn.database.actions.monitor

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.core.util.DebugLogger.log
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline

class MongoQueryMonitorMemberCount(database: Database) {
  def execute(relationId: Long): Long = {
    val pipeline = buildPipeline(relationId)
    log.debugElapsed {
      val memberCount = database.routes.aggregate(pipeline, classOf[CountResult], log).map(_.count).sum
      (s"$memberCount member count", memberCount)
    }
  }

  private def buildPipeline(relationId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("_id", relationId),
          equal("active", true)
        )
      ),
      project(
        fields(
          excludeId(),
          arraySize("count", "$structureRows")
        )
      )
    )
  }
}
