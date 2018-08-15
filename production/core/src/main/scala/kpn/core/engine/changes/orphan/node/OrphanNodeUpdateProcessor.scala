package kpn.core.engine.changes.orphan.node

import kpn.core.engine.changes.ChangeSetContext
import kpn.shared.changes.details.NodeChange

trait OrphanNodeUpdateProcessor {
  def process(context: ChangeSetContext, loadedNodeChange: LoadedNodeChange): Option[NodeChange]
}
