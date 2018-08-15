package kpn.core.engine.changes.network.update

import kpn.core.load.data.LoadedNetwork
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.shared.Timestamp

trait NetworkUpdateNetworkProcessor {
  def process(
    context: ChangeSetContext,
    loadedNetworkBefore: LoadedNetwork,
    loadedNetworkAfter: LoadedNetwork
  ): ChangeSetChanges
}
