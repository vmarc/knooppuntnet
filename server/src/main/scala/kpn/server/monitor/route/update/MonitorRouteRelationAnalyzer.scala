package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

trait MonitorRouteRelationAnalyzer {

  def analyzeReference(routeId: ObjectId, reference: MonitorRouteReference): Option[MonitorRouteState]
}
