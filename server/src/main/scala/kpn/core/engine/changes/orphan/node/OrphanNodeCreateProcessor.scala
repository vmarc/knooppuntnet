package kpn.core.engine.changes.orphan.node

import kpn.core.load.data.LoadedNode
import kpn.core.engine.changes.ChangeSetContext
import kpn.shared.changes.details.NodeChange

trait OrphanNodeCreateProcessor {
  def process(context: Option[ChangeSetContext], loadedNode: LoadedNode): Option[NodeChange]
}
