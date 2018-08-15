package kpn.core.engine.changes.network.create

import kpn.core.load.data.LoadedNetwork
import kpn.core.engine.changes.ChangeSetContext
import kpn.shared.Fact

trait NetworkCreateIgnoredProcessor {
  def process(context: ChangeSetContext, loadedNetwork: LoadedNetwork, ignoreReasons: Seq[Fact]): Unit
}
