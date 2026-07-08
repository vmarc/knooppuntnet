package kpn.server.analyzer.engine.monitor.changes

import kpn.api.common.changes.ChangeSet
import kpn.core.FastUtil
import kpn.server.analyzer.engine.context.ElementIds
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class MonitorChangeImpactAnalyzer {

  def hasImpact(changeSet: ChangeSet, routeId: Long, elementIds: ElementIds): Boolean = {
    changeSet.changes.exists { change =>
      change.nodes.exists(node => FastUtil.contains(elementIds.nodeIds, node.id)) ||
        change.ways.exists(way => FastUtil.contains(elementIds.wayIds, way.id)) ||
        change.relations.exists(relation => FastUtil.contains(elementIds.relationIds, relation.id))
    }
  }
}
