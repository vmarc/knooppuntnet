package kpn.database.actions.monitor

import kpn.api.common.monitor.MonitorRouteRelationInfo
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.Types.MongoPipeline
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

class MongoQueryMonitorRouteRelationInfos(database: Database) {
  def execute(relationIds: Seq[Long]): Seq[MonitorRouteRelationInfo] = {
    val pipeline = buildPipeline(relationIds)
    log.debugElapsed {
      val infos = database.routes.aggregate[MonitorRouteRelationInfo](pipeline, log)
      (s"${infos.length} route infos", infos)
    }
  }

  private def buildPipeline(relationIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("_id", relationIds: _*)
        )
      ),
      project(
        fields(
          excludeId(),
          computed("relationId", "$_id"),
          computed("name", "$summary.name"),
        )
      )
    )
  }
}
