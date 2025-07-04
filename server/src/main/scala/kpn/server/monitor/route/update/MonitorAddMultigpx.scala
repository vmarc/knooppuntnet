package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorAddMultigpx(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
) {

  private val log = Log(classOf[MonitorAddMultigpx])

  def execute(context: MonitorContext): Unit = {

    initReporter(context)

    monitorUpdateCommon.findGroup(context)
    verifyNewRoute(context)

    val (superSegmentCount: Long, osmDistance: Long) = context.value.update.relationId.flatMap(routeRepository.findRouteById) match {
      case Some(routeDoc) =>
        val sc: Long = routeDoc.superSegments.length
        val di: Long = routeDoc.superSegments.map(_.segments.map(_.relationSegment.meters).sum).sum
        (sc, di)
      case None => (0L, 0L)
    }

    val route = MonitorRoute(
      ObjectId(),
      context.value.group.get._id,
      context.value.update.routeName,
      context.value.update.description.getOrElse(""),
      context.value.update.comment,
      context.value.update.relationId,
      context.value.user,
      Time.now,
      None,
      analysisTimestamp = Some(Time.now),
      analysisDuration = None,
      referenceType = context.value.update.referenceType,
      referenceTimestamp = context.value.update.referenceTimestamp,
      referenceFilename = context.value.update.referenceFilename,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = superSegmentCount,
      osmDistance = osmDistance,
      relation = None,
      happy = false,
    )

    monitorRouteRepository.saveRoute(route)
    context.stepDone("save")
  }

  private def initReporter(context: MonitorContext): Unit = {
    context.value.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "save"),
          MonitorRouteUpdateStatusCommand("step-active", "save"),
        )
      )
    )
  }

  private def verifyNewRoute(context: MonitorContext): Unit = {
    val group = context.value.group.get
    val routeName = context.value.update.routeName
    monitorRouteRepository.routeByName(group._id, routeName) match {
      case None => // OK: no route with this name yet
      case Some(route) =>
        throw new IllegalStateException(
          s"""Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${group.name}""""
        )
    }
  }
}
