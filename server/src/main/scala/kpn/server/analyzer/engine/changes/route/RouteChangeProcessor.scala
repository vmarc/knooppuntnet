package kpn.server.analyzer.engine.changes.route

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait RouteChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetChanges
}
