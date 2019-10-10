package kpn.core.engine.changes.network

import kpn.core.engine.changes.ElementChanges
import kpn.shared.changes.ChangeSet

trait NetworkChangeAnalyzer {
  def analyze(changeSet: ChangeSet): ElementChanges
}
