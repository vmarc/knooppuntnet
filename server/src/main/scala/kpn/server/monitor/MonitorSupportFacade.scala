package kpn.server.monitor

import kpn.api.common.monitor.MonitorRouteRelation

trait MonitorSupportFacade {
  def routeStructure(routeRelationId: Long): Option[MonitorRouteRelation]
}
