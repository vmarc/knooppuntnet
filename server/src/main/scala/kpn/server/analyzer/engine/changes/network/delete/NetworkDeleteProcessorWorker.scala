package kpn.server.analyzer.engine.changes.network.delete

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait NetworkDeleteProcessorWorker {
  def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges
}
