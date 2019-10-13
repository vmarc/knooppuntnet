package kpn.server.analyzer.engine.changes.node

import kpn.core.analysis.NetworkNodeInfo
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.shared.changes.details.NodeChange

trait UnreferencedNodeProcessor {
  def process(context: ChangeSetContext, candidateUnreferencedNodes: Seq[NetworkNodeInfo]): Seq[NodeChange]
}
