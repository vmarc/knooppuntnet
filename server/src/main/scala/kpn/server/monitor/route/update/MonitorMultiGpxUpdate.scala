package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorMultiGpxUpdate(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
) {

  private val log = Log(classOf[MonitorMultiGpxUpdate])

  def initialMessage: MonitorMessage = {
    MonitorMessage(
      MonitorCommand.add("save"),
      MonitorCommand.active("save"),
    )
  }

  def execute(args: MonitorUpdateArgs, route: MonitorRoute, updatedRoute: MonitorRoute, now: Timestamp): Unit = {

    val route = buildRoute(args, updatedRoute)

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

  private def buildRoute(args: MonitorUpdateArgs, monitorRoute: MonitorRoute) = {
    monitorRoute.copy(
      analysisTimestamp = Some(Time.now),
      analysisDuration = None,
      referenceType = args.update.referenceType,
      referenceTimestamp = args.update.referenceTimestamp,
      referenceFilename = args.update.referenceFilename,
      referenceDistance = 0,
      deviationDistance = 0,
      deviationCount = 0,
      happy = false, // cannot be happy yet, there are no gpx references yet
    )
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    args.reporter.report(
      MonitorMessage(
        MonitorCommand.add("save"),
        MonitorCommand.active("save"),
      )
    )
  }
}
