package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteChangesPage

trait MonitorRouteChangesPageBuilder {

  def build(routeId: Long): Option[MonitorRouteChangesPage]

}
