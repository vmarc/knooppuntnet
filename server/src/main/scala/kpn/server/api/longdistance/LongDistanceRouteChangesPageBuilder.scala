package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangesPage

trait LongDistanceRouteChangesPageBuilder {

  def build(routeId: Long): Option[LongDistanceRouteChangesPage]

}
