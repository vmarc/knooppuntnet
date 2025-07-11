package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import org.springframework.stereotype.Component

@Component
class MonitorAdd(
  monitorMultiGpxAdd: MonitorMultiGpxAdd,
  monitorOsmNowAdd: MonitorOsmNowAdd,
  monitorOsmAdd: MonitorOsmAdd,
  monitorGpxAdd: MonitorGpxAdd
) {
  def execute(args: MonitorUpdateArgs): Unit = {
    args.update.referenceType match {
      case MonitorReferenceType.osmNow => monitorOsmNowAdd.execute(args)
      case MonitorReferenceType.osm => monitorOsmAdd.execute(args)
      case MonitorReferenceType.multiGpx => monitorMultiGpxAdd.execute(args)
      case MonitorReferenceType.gpx => monitorGpxAdd.execute(args)
    }
  }
}
