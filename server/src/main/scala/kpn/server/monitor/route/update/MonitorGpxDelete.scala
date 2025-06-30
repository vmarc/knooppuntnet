package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.util.Log
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorGpxDelete(
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave
) {

  private val log = Log(classOf[MonitorGpxDelete])

  def execute(context: MonitorContext): Unit = {

    initReporter(context)

    monitorUpdateCommon.findGroup(context)
    monitorUpdateCommon.findRoute(context)

    val relationId = context.value.update.relationId.getOrElse(throw new RuntimeException("subrelation id needed for gpx-delete"))
    context.deleteRouteReference(context.value.routeId, Some(relationId))
    monitorRouteRepository.deleteRouteReference(context.value.routeId, relationId)
    monitorRouteRepository.routeState(context.value.routeId, relationId) match {
      case None =>
      case Some(state) =>
        val updatedState = state.copy(
          matchesGeometry = None,
          deviations = Seq.empty,
        )
        monitorRouteRepository.saveRouteState(updatedState)
        context.set(
          context.value.copy(
            stateChanged = true
          )
        )
    }

    context.stepActive("save")
    monitorUpdateSave.save(context)
    context.stepDone("save")
  }

  private def initReporter(context: MonitorContext): Unit = {
    context.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand(
            "step-add",
            "delete",
          ),
          MonitorRouteUpdateStatusCommand(
            "step-add",
            "save"
          ),
          MonitorRouteUpdateStatusCommand(
            "step-active",
            "delete",
          ),
        )
      )
    )
  }
}
