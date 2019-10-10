package kpn.core.engine.changes.node

import kpn.core.analysis.NetworkNodeInfo
import kpn.core.engine.changes.ChangeSetContext
import kpn.shared.changes.details.NodeChange

trait UnreferencedNodeProcessor {
  def process(context: ChangeSetContext, candidateUnreferencedNodes: Seq[NetworkNodeInfo]): Seq[NodeChange]
}
