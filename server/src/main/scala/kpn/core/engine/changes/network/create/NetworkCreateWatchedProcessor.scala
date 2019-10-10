package kpn.core.engine.changes.network.create

import kpn.core.load.data.LoadedNetwork
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges

trait NetworkCreateWatchedProcessor {
  def process(context: ChangeSetContext, loadedNetwork: LoadedNetwork): ChangeSetChanges
}
