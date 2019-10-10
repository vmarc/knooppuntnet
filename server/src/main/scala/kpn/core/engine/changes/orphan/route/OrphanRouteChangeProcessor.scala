package kpn.core.engine.changes.orphan.route

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

trait OrphanRouteChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetChanges
}
