package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRouteChangesPage

trait LongdistanceRouteChangesPageBuilder {

  def build(routeId: Long): Option[LongdistanceRouteChangesPage]

}
