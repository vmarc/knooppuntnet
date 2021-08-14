package kpn.server.analyzer.engine.monitor

import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.server.analyzer.engine.context.ElementIds
import org.springframework.stereotype.Component

@Component
class MonitorChangeImpactAnalyzerImpl extends MonitorChangeImpactAnalyzer {

  override def hasImpact(changeSet: ChangeSet, routeId: Long, elementIds: ElementIds): Boolean = {
    changeSet.changes.exists { change =>
      change.elements.exists {
        case node: RawNode => elementIds.nodeIds.contains(node.id)
        case way: RawWay => elementIds.wayIds.contains(way.id)
        case relation: RawRelation => elementIds.relationIds.contains(relation.id)
        case _ => false
      }
    }
  }

}
