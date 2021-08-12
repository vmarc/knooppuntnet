package kpn.server.analyzer.engine.changes.node

import kpn.server.analyzer.engine.changes.ChangeSetContext

trait NewNodeChangeProcessor {

  def process(context: ChangeSetContext): ChangeSetContext

}
