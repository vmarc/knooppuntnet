package kpn.server.analyzer.engine.monitor.changes

import kpn.api.common.changes.ChangeSet
import kpn.server.analyzer.engine.context.RouteElementIds

trait MonitorChangeImpactAnalyzer {

  def hasImpact(changeSet: ChangeSet, routeId: Long, elementIds: RouteElementIds): Boolean
}
