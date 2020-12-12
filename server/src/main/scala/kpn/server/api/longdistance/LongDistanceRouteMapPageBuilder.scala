package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteMapPage

trait LongDistanceRouteMapPageBuilder {

  def build(routeId: Long): Option[LongDistanceRouteMapPage]

}
