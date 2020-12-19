package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRouteMapPage

trait LongdistanceRouteMapPageBuilder {

  def build(routeId: Long): Option[LongdistanceRouteMapPage]

}
