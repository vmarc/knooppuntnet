package kpn.database.actions.monitor

import kpn.api.common.Bounds
import kpn.core.util.DebugLogger.log
import kpn.core.util.Util.mergeBounds
import kpn.database.base.Database
import kpn.database.base.MongoProjections.objectIdToString
import kpn.database.base.Types.MongoPipeline
import kpn.server.monitor.domain.MonitorGroupRouteInfo
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

case class MonitorGroupRouteInfoData(
  groupId: String,
  monitorRouteId: String,
  bounds: Option[Bounds],
)

class MongoQueryMonitorGroupRouteInfos(database: Database) {
  def execute(): Seq[MonitorGroupRouteInfo] = {
    val pipeline = buildPipeline()
    log.debugElapsed {
      val routes = database.monitorRoutes.aggregate[MonitorGroupRouteInfoData](pipeline, log)
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
