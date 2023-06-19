package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.util.Log

class MonitorUpdateReporterLogger extends MonitorUpdateReporter {
  private val log = Log(classOf[MonitorUpdateReporterLogger])

  override def report(message: MonitorRouteUpdateStatusMessage): Unit = {
    log.info(s"message=$message")
  }
}
