package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState

trait MonitorRouteRelationAnalyzer {

  def analyzeReference(routeId: ObjectId, reference: MonitorRouteReference): Option[MonitorRouteState]
}
