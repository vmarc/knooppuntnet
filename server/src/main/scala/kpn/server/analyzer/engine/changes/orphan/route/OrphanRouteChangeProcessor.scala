package kpn.server.analyzer.engine.changes.orphan.route

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait OrphanRouteChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetChanges
}
