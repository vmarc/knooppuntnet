package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.core.util.Log

class MonitorUpdateReporterLogger extends MonitorUpdateReporter {
  private val log = Log(classOf[MonitorUpdateReporterLogger])

  override def report(status: MonitorRouteUpdateStatus): Unit = {
    log.info(s"status=$status")
  }
}
