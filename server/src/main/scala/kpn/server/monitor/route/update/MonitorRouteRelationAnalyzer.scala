package kpn.server.monitor.route.update

import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState

trait MonitorRouteRelationAnalyzer {

  def analyzeReference(context: MonitorUpdateContext, reference: MonitorRouteReference): Option[MonitorRouteState]
}
