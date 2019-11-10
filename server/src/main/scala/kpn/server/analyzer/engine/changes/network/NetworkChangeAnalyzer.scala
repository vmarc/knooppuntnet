package kpn.server.analyzer.engine.changes.network

import kpn.api.common.changes.ChangeSet
import kpn.server.analyzer.engine.changes.ElementChanges

trait NetworkChangeAnalyzer {
  def analyze(changeSet: ChangeSet): ElementChanges
}
