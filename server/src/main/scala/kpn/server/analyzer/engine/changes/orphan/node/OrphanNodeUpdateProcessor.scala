package kpn.server.analyzer.engine.changes.orphan.node

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.shared.changes.details.NodeChange

trait OrphanNodeUpdateProcessor {
  def process(context: ChangeSetContext, loadedNodeChange: LoadedNodeChange): Option[NodeChange]
}
