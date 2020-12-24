package kpn.server.analyzer.engine.monitor

import kpn.api.common.changes.ChangeSet
import kpn.server.analyzer.engine.changes.changes.ElementIds

trait MonitorChangeImpactAnalyzer {

  def hasImpact(changeSet: ChangeSet, routeId: Long, elementIds: ElementIds): Boolean

}
