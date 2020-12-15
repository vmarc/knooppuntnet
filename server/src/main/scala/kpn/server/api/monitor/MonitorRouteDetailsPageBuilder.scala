package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteDetailsPage

trait MonitorRouteDetailsPageBuilder {

  def build(routeId: Long): Option[MonitorRouteDetailsPage]

}
