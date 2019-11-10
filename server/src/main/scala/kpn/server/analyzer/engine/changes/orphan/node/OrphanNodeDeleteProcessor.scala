package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.NodeChange
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait OrphanNodeDeleteProcessor {
  def process(context: ChangeSetContext, loadedNodeDelete: LoadedNodeDelete): Option[NodeChange]
}
