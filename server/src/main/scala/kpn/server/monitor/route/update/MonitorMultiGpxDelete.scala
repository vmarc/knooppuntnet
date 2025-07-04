package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.util.Log
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorMultiGpxDelete(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave
) {

  private val log = Log(classOf[MonitorMultiGpxDelete])

  def execute(context: MonitorContext): Unit = {
    val args = MonitorUpdateArgs(
      context.value.user,
      context.value.reporter,
      context.value.update,
    )
    newExecute(args)
  }

  private def newExecute(args: MonitorUpdateArgs): Unit = {

    initReporter(args)

    val group = monitorUpdateCommon.findGroup(args)
    val route = monitorUpdateCommon.findRoute(args, group)

    val superRelationId = route.relationId.getOrElse(throw new RuntimeException("route relation id needed for gpx-delete"))
    val subRelationId = args.update.relationId.getOrElse(throw new RuntimeException("subrelation id needed for gpx-delete"))

    monitorRouteRepository.deleteRouteReference(route._id, subRelationId)
    monitorRouteRepository.deleteRouteState(route._id, subRelationId)

    val updatedRoute = monitorUpdateCommon.updateSuperRoute(route)

    args.reporter.stepActive("save")
    monitorRouteRepository.saveRoute(updatedRoute)
    args.reporter.stepDone("save")
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "delete"),
          MonitorRouteUpdateStatusCommand("step-add", "save"),
          MonitorRouteUpdateStatusCommand("step-active", "delete"),
        )
      )
    )
  }
}
