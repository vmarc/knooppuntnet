package kpn.server.analyzer.engine.changes.network.main

import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait NetworkChangeProcessor extends ChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetContext
}
