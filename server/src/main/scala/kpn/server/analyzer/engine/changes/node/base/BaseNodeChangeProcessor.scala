package kpn.server.analyzer.engine.changes.node.base

import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext

trait BaseNodeChangeProcessor extends ChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetContext
}
