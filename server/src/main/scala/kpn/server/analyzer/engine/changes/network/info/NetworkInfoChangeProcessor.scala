package kpn.server.analyzer.engine.changes.network.info

import kpn.server.analyzer.engine.changes.ChangeSetContext

trait NetworkInfoChangeProcessor {
  def analyze(context: ChangeSetContext): ChangeSetContext
}
