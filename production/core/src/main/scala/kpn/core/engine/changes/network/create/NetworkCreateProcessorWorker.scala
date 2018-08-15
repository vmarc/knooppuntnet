package kpn.core.engine.changes.network.create

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

trait NetworkCreateProcessorWorker {
  def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges
}
