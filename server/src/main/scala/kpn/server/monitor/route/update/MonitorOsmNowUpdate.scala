package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorOsmNowUpdate(
  monitorRouteRepository: MonitorRouteRepository,
  monitorOsmNowAnalyze: MonitorOsmNowAnalyze
) {

  private val log = Log(classOf[MonitorOsmNowUpdate])

  def initialMessage: MonitorRouteUpdateStatusMessage = {
    MonitorRouteUpdateStatusMessage(
      commands = Seq(
        MonitorRouteUpdateStatusCommand("step-add", "prepare"),
        MonitorRouteUpdateStatusCommand("step-active", "prepare"),
      )
    )
  }

  def execute(
    group: MonitorGroup,
    args: MonitorUpdateArgs,
    route: MonitorRoute,
    updatedRoute: MonitorRoute,
    now: Timestamp,
    analysisStartMillis: Long
  ): Unit = {

    if (args.update.relationId.isEmpty) {
      args.reporter.report(
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-add", "save"),
            MonitorRouteUpdateStatusCommand("step-active", "save"),
          )
        )
      )
      monitorRouteRepository.saveRoute(updatedRoute)
      args.reporter.stepDone("save")
    }
    else {
      args.reporter.report(
        MonitorRouteUpdateStatusMessage(
          commands = Seq(
            MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
            MonitorRouteUpdateStatusCommand("step-active", "analyze-route-structure"),
          )
        )
      )

      monitorOsmNowAnalyze.execute(group, args, now, route._id, analysisStartMillis)
    }
  }
}
