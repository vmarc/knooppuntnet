package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteUpdate

trait MonitorUpdateRoute {
  def update(context: MonitorUpdateContext, user: String, update: MonitorRouteUpdate): MonitorUpdateContext
}
