package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorMultiGpxAdd(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
) {

  private val log = Log(classOf[MonitorMultiGpxAdd])

  def execute(args: MonitorUpdateArgs): Unit = {

    initReporter(args)

    val group = monitorUpdateCommon.findGroup(args)
    monitorUpdateCommon.verifyNewRoute(group, args)

    val (superSegmentCount, osmDistance) = getRouteInfo(args)
    val route = buildRoute(args, group, superSegmentCount, osmDistance)

    monitorRouteRepository.saveRoute(route)
    args.reporter.stepDone("save")
  }

  private def getRouteInfo(args: MonitorUpdateArgs) = {
    routeRepository.findRouteById(args.relationId) match {
      case Some(routeDoc) =>
        val sc: Long = routeDoc.superSegments.length
        val di: Long = routeDoc.superDistance
        (sc, di)
      case None => (0L, 0L)
    }
  }

  private def buildRoute(args: MonitorUpdateArgs, group: MonitorGroup, superSegmentCount: Long, osmDistance: Long) = {
    MonitorRoute(
      ObjectId(),
      group._id,
      args.update.routeName,
      args.update.description.getOrElse(""),
      args.update.comment,
      args.update.relationId,
      args.user,
      Time.now,
      None,
      analysisTimestamp = Some(Time.now),
      analysisDuration = None,
      referenceType = args.update.referenceType,
      referenceTimestamp = args.update.referenceTimestamp,
      referenceFilename = args.update.referenceFilename,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      osmSegmentCount = superSegmentCount,
      osmDistance = osmDistance,
      relation = None,
      happy = false, // cannot be happy yet, there are no gpx references yet
    )
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "save"),
          MonitorRouteUpdateStatusCommand("step-active", "save"),
        )
      )
    )
  }
}
