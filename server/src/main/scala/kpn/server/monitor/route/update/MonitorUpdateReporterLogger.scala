package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorMessage
import kpn.core.util.Log

class MonitorUpdateReporterLogger extends MonitorUpdateReporter {
  private val log = Log(classOf[MonitorUpdateReporterLogger])

  override def report(message: MonitorMessage): Unit = {
    log.info(s"message=$message")
  }
}
