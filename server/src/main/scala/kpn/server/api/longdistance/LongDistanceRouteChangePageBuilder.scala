package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangePage

trait LongDistanceRouteChangePageBuilder {

  def build(routeId: Long, changeId: Long): Option[LongDistanceRouteChangePage]

}
