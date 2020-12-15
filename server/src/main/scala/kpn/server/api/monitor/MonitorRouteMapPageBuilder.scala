package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteMapPage

trait MonitorRouteMapPageBuilder {

  def build(routeId: Long): Option[MonitorRouteMapPage]

}
