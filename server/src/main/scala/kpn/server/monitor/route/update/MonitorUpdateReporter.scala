package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteUpdateStatus

trait MonitorUpdateReporter {

  def report(status: MonitorRouteUpdateStatus): Unit
}
