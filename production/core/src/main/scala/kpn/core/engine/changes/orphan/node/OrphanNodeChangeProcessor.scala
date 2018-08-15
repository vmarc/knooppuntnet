package kpn.core.engine.changes.orphan.node

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

trait OrphanNodeChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetChanges
}
