package kpn.database.actions.monitor

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.in
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.monitor.MonitorRouteRelationInfo
import kpn.core.util.DebugLogger.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

class MongoQueryMonitorRouteRelationInfos(database: Database) {
  def execute(relationIds: Seq[Long]): Seq[MonitorRouteRelationInfo] = {
    val pipeline = buildPipeline(relationIds)
    log.debugElapsed {
      val infos = database.routes.aggregate(pipeline, classOf[MonitorRouteRelationInfo], log)
      (s"${infos.length} route infos", infos)
    }
  }

  private def buildPipeline(relationIds: Seq[Long]): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          in("_id", relationIds *)
        )
      ),
      project(
        fields(
          excludeId(),
          computed("relationId", "$_id"),
          computed("name", "$base.summary.name"),
        )
      )
    )
  }
}
