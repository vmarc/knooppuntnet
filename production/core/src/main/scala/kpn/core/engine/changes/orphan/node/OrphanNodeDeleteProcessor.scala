package kpn.core.engine.changes.orphan.node

import kpn.core.engine.changes.ChangeSetContext
import kpn.shared.changes.details.NodeChange

trait OrphanNodeDeleteProcessor {
  def process(context: ChangeSetContext, loadedNodeDelete: LoadedNodeDelete): Option[NodeChange]
}
