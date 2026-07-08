package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorOsmUpdate(
  monitorRouteRepository: MonitorRouteRepository,
  monitorOsmAnalyze: MonitorOsmAnalyze
) {

  private val log = Log(classOf[MonitorOsmUpdate])

  def initialMessage: MonitorMessage = {
    MonitorMessage(
      MonitorCommand.add("prepare"),
      MonitorCommand.active("prepare"),
    )
  }

  def execute(
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

      monitorOsmAnalyze.execute(
        updatedRoute,
        now,
        args,
        args.referenceTimestamp,
        analysisStartMillis
      )
    }
  }
}
