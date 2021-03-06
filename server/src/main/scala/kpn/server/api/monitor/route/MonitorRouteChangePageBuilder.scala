package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteChangePage

trait MonitorRouteChangePageBuilder {

  def build(routeId: Long, changeSetId: Long, replicationId: Long): Option[MonitorRouteChangePage]

}
