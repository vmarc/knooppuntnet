package kpn.database.actions.monitor

import kpn.core.util.DebugLogger.log
import kpn.database.base.CountResult
import kpn.database.base.Database
import kpn.database.base.MongoProjections.arraySize
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

class MongoQueryMonitorMemberCount(database: Database) {
  def execute(relationId: Long): Long = {
    val pipeline = buildPipeline(relationId)
    log.debugElapsed {
      val memberCount = database.routes.aggregate[CountResult](pipeline, log).map(_.count).sum
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
