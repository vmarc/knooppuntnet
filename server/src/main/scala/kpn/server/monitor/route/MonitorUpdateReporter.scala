package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteUpdateStatus

trait MonitorUpdateReporter {

  def report(status: MonitorRouteUpdateStatus): Unit
}
