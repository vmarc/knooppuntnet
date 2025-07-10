package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.util.Log
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

  def execute(args: MonitorUpdateArgs): Unit = {

    val now = Time.now
    val analysisStartMillis = System.currentTimeMillis()

    initReporter(args)

    val group = monitorUpdateCommon.findGroup(args)
    monitorUpdateCommon.verifyNewRoute(group, args)

    val monitorRouteId = ObjectId()

    monitorOsmNowAnalyze.execute(group, args, now, monitorRouteId, analysisStartMillis)
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "prepare"),
          MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
          MonitorRouteUpdateStatusCommand("step-active", "prepare"),
        )
      )
    )
  }
}
