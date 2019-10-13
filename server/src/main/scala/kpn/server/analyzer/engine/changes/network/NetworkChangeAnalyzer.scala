package kpn.server.analyzer.engine.changes.network

import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.shared.changes.ChangeSet

trait NetworkChangeAnalyzer {
  def analyze(changeSet: ChangeSet): ElementChanges
}
