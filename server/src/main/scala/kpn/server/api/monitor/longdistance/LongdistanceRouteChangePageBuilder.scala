package kpn.server.api.monitor.longdistance

import kpn.api.common.monitor.LongdistanceRouteChangePage

trait LongdistanceRouteChangePageBuilder {

  def build(routeId: Long, changeId: Long): Option[LongdistanceRouteChangePage]

}
