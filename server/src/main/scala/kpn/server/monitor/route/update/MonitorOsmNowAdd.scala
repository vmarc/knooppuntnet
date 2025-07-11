package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorOsmNowAdd(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorOsmNowAnalyze: MonitorOsmNowAnalyze
) {

  private val log = Log(classOf[MonitorOsmNowAdd])
  val geometryFactory = new GeometryFactory

  def execute(group: MonitorGroup, args: MonitorUpdateArgs, now: Timestamp, analysisStartMillis: Long): Unit = {
    val monitorRouteId = ObjectId()
    monitorOsmNowAnalyze.execute(group, args, now, monitorRouteId, analysisStartMillis)
  }

  def initialMessage: MonitorMessage = {
    MonitorMessage(
      MonitorCommand.add("prepare"),
      MonitorCommand.add("analyze-route-structure"),
      MonitorCommand.active("prepare"),
    )
  }
}
