package kpn.server.analyzer.engine.changes.orphan.node

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait OrphanNodeChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetChanges
}
