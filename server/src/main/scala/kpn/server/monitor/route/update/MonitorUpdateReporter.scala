package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage

trait MonitorUpdateReporter {

  def report(message: MonitorRouteUpdateStatusMessage): Unit
}
