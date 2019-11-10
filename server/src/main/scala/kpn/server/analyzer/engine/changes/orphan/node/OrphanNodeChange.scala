package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.custom.Fact

object OrphanNodeChange {

  def isOrphanNodeChange(nodeChange: NodeChange): Boolean = {
      val facts = nodeChange.facts
      nodeChange.changeType match {
        case ChangeType.Create => facts.contains(Fact.OrphanNode)
        case ChangeType.Update =>
          facts.contains(Fact.OrphanNode) ||
            (facts.contains(Fact.WasOrphan) && facts.contains(Fact.LostHikingNodeTag)) ||
            (facts.contains(Fact.WasOrphan) && facts.contains(Fact.LostBicycleNodeTag))
        case ChangeType.Delete => facts.contains(Fact.OrphanNode) || facts.contains(Fact.WasOrphan)
        case _ => false
      }
  }
}
