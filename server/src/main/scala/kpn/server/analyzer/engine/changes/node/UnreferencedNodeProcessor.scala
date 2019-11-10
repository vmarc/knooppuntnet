package kpn.server.analyzer.engine.changes.node

import kpn.api.common.changes.details.NodeChange
import kpn.core.analysis.NetworkNodeInfo
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait UnreferencedNodeProcessor {
  def process(context: ChangeSetContext, candidateUnreferencedNodes: Seq[NetworkNodeInfo]): Seq[NodeChange]
}
