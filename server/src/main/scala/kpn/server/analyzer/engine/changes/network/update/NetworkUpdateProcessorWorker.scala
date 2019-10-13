package kpn.server.analyzer.engine.changes.network.update

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait NetworkUpdateProcessorWorker {
  def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges
}
