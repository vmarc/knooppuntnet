package kpn.server.analyzer.engine.changes.network

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait NetworkChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetChanges
}
