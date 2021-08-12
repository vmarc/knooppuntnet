package kpn.server.analyzer.engine.changes.network

import kpn.server.analyzer.engine.changes.ChangeSetContext

trait NetworkChangeProcessor {
  def process(context: ChangeSetContext): ChangeSetContext
}
