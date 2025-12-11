package kpn.server.analyzer.engine.changes.network.base

import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait BaseNetworkChangeProcessor extends ChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetContext
}
