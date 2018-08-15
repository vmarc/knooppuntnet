package kpn.core.engine.changes.network.delete

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

trait NetworkDeleteProcessorWorker {
  def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges
}
