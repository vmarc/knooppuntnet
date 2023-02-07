package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteProperties

trait MonitorUpdateRoute {
  def update(context: MonitorUpdateContext, user: String, properties: MonitorRouteProperties): MonitorUpdateContext
}
