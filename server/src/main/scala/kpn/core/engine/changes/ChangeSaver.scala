package kpn.core.engine.changes

import kpn.core.engine.changes.data.ChangeSetChanges
import kpn.shared.ReplicationId
import kpn.shared.changes.ChangeSet

trait ChangeSaver {
  def save(replicationId: ReplicationId, changeSet: ChangeSet, changes: ChangeSetChanges): Unit
}
