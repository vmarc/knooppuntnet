package kpn.server.analyzer.engine.changes.network.create

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait NetworkCreateProcessorWorker {
  def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges
}
