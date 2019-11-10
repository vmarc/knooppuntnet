package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.NodeChange
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait OrphanNodeUpdateProcessor {
  def process(context: ChangeSetContext, loadedNodeChange: LoadedNodeChange): Option[NodeChange]
}
