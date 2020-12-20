package kpn.server.api.monitor.longdistance

import kpn.api.common.monitor.LongdistanceRouteMapPage

trait LongdistanceRouteMapPageBuilder {

  def build(routeId: Long): Option[LongdistanceRouteMapPage]

}
