package kpn.database.actions.monitor

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.Bounds
import kpn.core.util.DebugLogger.log
import kpn.core.util.Util.mergeBounds
import kpn.database.base.Database
import kpn.database.base.MongoProjections.objectIdToString
import kpn.database.base.Types.MongoPipeline
import kpn.server.monitor.domain.MonitorGroupRouteInfo

case class MonitorGroupRouteInfoData(
  groupId: String,
  monitorRouteId: String,
  bounds: Option[Bounds],
)

class MongoQueryMonitorGroupRouteInfos(database: Database) {
  def execute(): Seq[MonitorGroupRouteInfo] = {
    val pipeline = buildPipeline()
    log.debugElapsed {
      val routes = database.monitorRoutes.aggregate(pipeline, classOf[MonitorGroupRouteInfoData], log)
      val infos = routes.groupBy(_.groupId).toSeq.map { case (groupId, datas) =>
        val routeBounds = datas.flatMap(_.bounds)
        val bounds = Option.when(routeBounds.nonEmpty) {
          mergeBounds(datas.flatMap(_.bounds))
        }
        val monitorRouteIds = datas.map(_.monitorRouteId)
        MonitorGroupRouteInfo(groupId, monitorRouteIds, bounds)
      }
      (s"${infos.length} groups", infos)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      project(
        fields(
          excludeId(),
          objectIdToString("groupId"),
          objectIdToString("monitorRouteId", "$_id"),
          include("bounds"),
        )
      ),
      sort(
        orderBy(
          ascending("routeId"),
          ascending("relationId"),
        )
      )
    )
  }
}
