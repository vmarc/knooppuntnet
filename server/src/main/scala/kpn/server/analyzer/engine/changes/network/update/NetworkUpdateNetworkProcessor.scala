package kpn.server.analyzer.engine.changes.network.update

import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.shared.Timestamp

trait NetworkUpdateNetworkProcessor {
  def process(
    context: ChangeSetContext,
    loadedNetworkBefore: LoadedNetwork,
    loadedNetworkAfter: LoadedNetwork
  ): ChangeSetChanges
}
