package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.core.common.Time
import org.springframework.stereotype.Component

@Component
class MonitorAdd(
  monitorOsmAdd: MonitorOsmAdd,
  monitorOsmNowAdd: MonitorOsmNowAdd,
  monitorGpxAdd: MonitorGpxAdd,
  monitorMultiGpxAdd: MonitorMultiGpxAdd,
  monitorUpdateCommon: MonitorUpdateCommon,
) {
  def execute(args: MonitorUpdateArgs): Unit = {

    initReporter(args)

    val now = Time.now
    val analysisStartMillis = System.currentTimeMillis()

    val group = monitorUpdateCommon.findGroup(args)
    monitorUpdateCommon.verifyNewRoute(group, args)

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
}
