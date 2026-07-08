package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorOsmNowUpdate(
  monitorRouteRepository: MonitorRouteRepository,
  monitorOsmNowAnalyze: MonitorOsmNowAnalyze
) {

  private val log = Log(classOf[MonitorOsmNowUpdate])

  def initialMessage: MonitorMessage = {
    MonitorMessage(
      MonitorCommand.add("prepare"),
      MonitorCommand.active("prepare"),
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
        MonitorMessage(
          MonitorCommand.add("save"),
          MonitorCommand.active("save"),
        )
      )
      monitorRouteRepository.saveRoute(updatedRoute)
      args.reporter.stepDone("save")
    }
    else {
      args.reporter.report(
        MonitorMessage(
          MonitorCommand.add("analyze-route-structure"),
          MonitorCommand.active("analyze-route-structure"),
        )
      )

      monitorOsmNowAnalyze.execute(group, args, now, route._id, analysisStartMillis)
    }
  }
}
