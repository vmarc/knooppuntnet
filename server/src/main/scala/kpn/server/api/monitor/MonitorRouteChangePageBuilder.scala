package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteChangePage

trait MonitorRouteChangePageBuilder {

  def build(routeId: Long, changeId: Long): Option[MonitorRouteChangePage]

}
