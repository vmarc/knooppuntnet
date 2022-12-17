package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteRelation

trait MonitorSupportFacade {
  def routeStructure(routeRelationId: Long): Option[MonitorRouteRelation]
}
