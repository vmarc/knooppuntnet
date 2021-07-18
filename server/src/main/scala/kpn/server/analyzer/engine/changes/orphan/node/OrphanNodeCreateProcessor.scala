package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.NodeChange
import kpn.api.common.data.raw.RawNode
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait OrphanNodeCreateProcessor {
  def process(context: Option[ChangeSetContext], node: RawNode): Option[NodeChange]
}
