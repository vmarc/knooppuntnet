package kpn.server.analyzer.engine.monitor.changes

import kpn.api.common.changes.ChangeSet
import kpn.server.analyzer.engine.context.ElementIds

trait MonitorChangeImpactAnalyzer {

  def hasImpact(changeSet: ChangeSet, routeId: Long, elementIds: ElementIds): Boolean

}
