package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.NodeChange
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.load.data.LoadedNode

trait OrphanNodeCreateProcessor {
  def process(context: Option[ChangeSetContext], loadedNode: LoadedNode): Option[NodeChange]
}
