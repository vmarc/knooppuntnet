package kpn.server.analyzer.engine.changes.network.create

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.load.data.LoadedNetwork

trait NetworkCreateWatchedProcessor {
  def process(context: ChangeSetContext, loadedNetwork: LoadedNetwork): ChangeSetChanges
}
