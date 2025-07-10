package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class MonitorUpdate(
  monitorOsmUpdate: MonitorOsmUpdate,
  monitorGpxUpdate: MonitorGpxUpdate,
) {

  private val log = Log(classOf[MonitorUpdate])

  def execute(args: MonitorUpdateArgs): Unit = {

    if (args.update.referenceType == MonitorReferenceType.osm) {
      monitorOsmUpdate.execute(args)
    }
    else if (args.update.referenceType == MonitorReferenceType.gpx) {
      monitorGpxUpdate.execute(args)
    }
    else {
      throw new RuntimeException(s"invalid reference type ${args.update.referenceType} for update")
    }
  }
}
