package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.ChangeSet
import kpn.server.analyzer.engine.changes.ElementChanges

trait NodeChangeAnalyzer {
  def analyze(changeSet: ChangeSet): ElementChanges
}
