package kpn.server.analyzer.engine.changes.orphan.node

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.shared.changes.details.NodeChange

trait OrphanNodeDeleteProcessor {
  def process(context: ChangeSetContext, loadedNodeDelete: LoadedNodeDelete): Option[NodeChange]
}
