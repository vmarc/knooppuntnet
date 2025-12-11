package kpn.server.analyzer.engine.changes.node.main

import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait NodeChangeProcessor extends ChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetContext
}
