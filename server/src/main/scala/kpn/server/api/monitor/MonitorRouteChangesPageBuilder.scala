package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteChangesPage

trait MonitorRouteChangesPageBuilder {

  def build(routeId: Long): Option[MonitorRouteChangesPage]

}
