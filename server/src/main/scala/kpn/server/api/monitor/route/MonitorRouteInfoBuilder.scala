package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteInfoPage

trait MonitorRouteInfoBuilder {

  def build(routeId: Long): MonitorRouteInfoPage

}
