package kpn.core.engine.changes.orphan.node

import kpn.shared.changes.ChangeSet

trait OrphanNodeChangeAnalyzer {
  def analyze(changeSet: ChangeSet): OrphanNodeChangeAnalysis
}
