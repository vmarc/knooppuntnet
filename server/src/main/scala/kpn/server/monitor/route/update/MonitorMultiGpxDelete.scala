package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
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

    initReporter(context)

    monitorUpdateCommon.findGroup(context)
    monitorUpdateCommon.findRoute(context)

    val relationId = context.value.update.relationId.getOrElse(throw new RuntimeException("subrelation id needed for gpx-delete"))

    monitorRouteRepository.deleteRouteReference(context.value.routeId, relationId)
    monitorRouteRepository.deleteRouteState(context.value.routeId, relationId)

    // duplicated from MonitorMultiGpxUpload
    val references = monitorRouteRepository.routeReferences(context.value.routeId)
    val referenceDistance = references.map(_.referenceDistance).sum
    val states = monitorRouteRepository.routeStates(context.value.routeId)
    val deviationCount = states.map(_.deviations.length).sum
    val deviationDistance = states.map(_.deviations.length).sum
    val matchesDistance = states.map(_.matchesDistance).sum

    val (superSegmentCount: Long, osmDistance: Long) = context.value.relationId.flatMap(routeRepository.findRouteById) match {
      case Some(routeDoc) =>
        val sc = routeDoc.superSegments.length.toLong
        val di = routeDoc.superSegments.map(_.segments.map(_.relationSegment.meters).sum).sum
        (sc, di)
      case None => (0L, 0L)
    }

    val updatedRoute = context.value.route.copy(
      analysisTimestamp = Some(Time.now),
      referenceDistance = referenceDistance,
      deviationCount = deviationCount,
      deviationDistance = deviationDistance,
      osmSegmentCount = superSegmentCount,
      happy = false
    )
    context.stepActive("save")
    monitorRouteRepository.saveRoute(updatedRoute)
    context.stepDone("save")
  }

  private def initReporter(context: MonitorContext): Unit = {
    context.report(
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
