package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.core.common.Time
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorAdd(
  monitorOsmAdd: MonitorOsmAdd,
  monitorOsmNowAdd: MonitorOsmNowAdd,
  monitorGpxAdd: MonitorGpxAdd,
  monitorMultiGpxAdd: MonitorMultiGpxAdd,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorRouteRepository: MonitorRouteRepository
) {
  def execute(args: MonitorUpdateArgs): Unit = {

    initReporter(args)

    val now = Time.now
    val analysisStartMillis = System.currentTimeMillis()

    val group = monitorUpdateCommon.findGroup(args)
    verifyNewRoute(group, args)

    args.update.referenceType match {
      case MonitorReferenceType.osm => monitorOsmAdd.execute(group, args, now, analysisStartMillis)
      case MonitorReferenceType.osmNow => monitorOsmNowAdd.execute(group, args, now, analysisStartMillis)
      case MonitorReferenceType.gpx => monitorGpxAdd.execute(group, args, now)
      case MonitorReferenceType.multiGpx => monitorMultiGpxAdd.execute(group, args)
    }
  }

  private def initReporter(args: MonitorUpdateArgs): Unit = {
    val initialMessage = args.update.referenceType match {
      case MonitorReferenceType.osm => monitorOsmAdd.initialMessage
      case MonitorReferenceType.osmNow => monitorOsmNowAdd.initialMessage
      case MonitorReferenceType.gpx => monitorGpxAdd.initialMessage
      case MonitorReferenceType.multiGpx => monitorMultiGpxAdd.initialMessage
    }
    args.reporter.report(
      initialMessage
    )
  }

  private def verifyNewRoute(group: MonitorGroup, args: MonitorUpdateArgs): Unit = {
    val routeName = args.update.routeName
    monitorRouteRepository.routeByName(group._id, routeName) match {
      case None => // OK: no route with this name yet
      case Some(route) =>
        throw new IllegalStateException(
          s"""Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${group.name}""""
        )
    }
  }
}
