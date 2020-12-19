package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRouteDetailsPage

trait LongdistanceRouteDetailsPageBuilder {

  def build(routeId: Long): Option[LongdistanceRouteDetailsPage]

}
