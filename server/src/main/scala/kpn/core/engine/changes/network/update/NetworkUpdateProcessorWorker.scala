package kpn.core.engine.changes.network.update

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

trait NetworkUpdateProcessorWorker {
  def process(context: ChangeSetContext, networkId: Long): ChangeSetChanges
}
