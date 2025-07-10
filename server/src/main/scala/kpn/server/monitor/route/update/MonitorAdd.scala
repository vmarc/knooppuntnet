package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class MonitorAdd(
  monitorMultiGpxAdd: MonitorMultiGpxAdd,
  monitorOsmNowAdd: MonitorOsmNowAdd,
  monitorOsmAdd: MonitorOsmAdd,
  monitorGpxAdd: MonitorGpxAdd
) {

  private val log = Log(classOf[MonitorAdd])

  def execute(args: MonitorUpdateArgs): Unit = {
    if (args.update.referenceType == MonitorReferenceType.osm) {
      if (args.update.referenceNow.contains(true)) {
        monitorOsmNowAdd.execute(args)
      }
      else {
        monitorOsmAdd.execute(args)
      }
    }
    else if (args.update.referenceType == MonitorReferenceType.multiGpx) {
      monitorMultiGpxAdd.execute(args)
    }
    else if (args.update.referenceType == MonitorReferenceType.gpx) {
      monitorGpxAdd.execute(args)
    }
    else {
      throw new RuntimeException(s"invalid reference type ${args.update.referenceType} for add")
    }
  }
}
