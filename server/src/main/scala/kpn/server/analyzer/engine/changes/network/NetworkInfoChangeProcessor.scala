package kpn.server.analyzer.engine.changes.network

import kpn.server.analyzer.engine.changes.ChangeSetContext

trait NetworkInfoChangeProcessor {

  def analyze(context: ChangeSetContext): ChangeSetContext

}
