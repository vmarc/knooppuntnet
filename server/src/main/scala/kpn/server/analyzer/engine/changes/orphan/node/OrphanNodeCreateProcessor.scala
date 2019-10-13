package kpn.server.analyzer.engine.changes.orphan.node

import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.shared.changes.details.NodeChange

trait OrphanNodeCreateProcessor {
  def process(context: Option[ChangeSetContext], loadedNode: LoadedNode): Option[NodeChange]
}
