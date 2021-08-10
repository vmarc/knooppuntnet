package kpn.server.analyzer.engine.changes.node

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait NodeChangeProcessor {
  def process(context: ChangeSetContext, impactedNodeIds: Seq[Long]): ChangeSetChanges
}
