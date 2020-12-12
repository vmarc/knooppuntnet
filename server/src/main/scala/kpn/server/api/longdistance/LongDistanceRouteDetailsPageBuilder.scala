package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteDetailsPage

trait LongDistanceRouteDetailsPageBuilder {

  def build(routeId: Long): Option[LongDistanceRouteDetailsPage]

}
