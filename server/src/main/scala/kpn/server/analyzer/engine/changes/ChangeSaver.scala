package kpn.server.analyzer.engine.changes

import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.shared.ReplicationId
import kpn.shared.changes.ChangeSet

trait ChangeSaver {
  def save(replicationId: ReplicationId, changeSet: ChangeSet, changes: ChangeSetChanges): Unit
}
