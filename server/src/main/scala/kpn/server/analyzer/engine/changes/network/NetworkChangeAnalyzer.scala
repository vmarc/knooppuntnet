package kpn.server.analyzer.engine.changes.network

import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges

trait NetworkChangeAnalyzer {
  def analyze(context: ChangeSetContext): ElementChanges
}
