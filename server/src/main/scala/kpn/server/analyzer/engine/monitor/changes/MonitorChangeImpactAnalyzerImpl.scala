package kpn.server.analyzer.engine.monitor.changes

import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.core.FastUtil
import kpn.server.analyzer.engine.context.RouteElementIds
import org.springframework.stereotype.Component

@Component
class MonitorChangeImpactAnalyzerImpl extends MonitorChangeImpactAnalyzer {

  override def hasImpact(changeSet: ChangeSet, routeId: Long, elementIds: RouteElementIds): Boolean = {
    changeSet.changes.exists { change =>
      change.elements.exists {
        case node: RawNode => FastUtil.contains(elementIds.nodeIds, node.id)
        case way: RawWay => FastUtil.contains(elementIds.wayIds, way.id)
        case relation: RawRelation => FastUtil.contains(elementIds.relationIds, relation.id)
        case _ => false
      }
    }
  }
}
