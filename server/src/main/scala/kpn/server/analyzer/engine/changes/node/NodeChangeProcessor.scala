package kpn.server.analyzer.engine.changes.node

import kpn.server.analyzer.engine.changes.ChangeSetContext

trait NodeChangeProcessor {

  def process(context: ChangeSetContext): ChangeSetContext

}
