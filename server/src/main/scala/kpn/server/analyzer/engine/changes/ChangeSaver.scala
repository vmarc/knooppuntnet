package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

trait ChangeSaver {
  def save(replicationId: ReplicationId, changeSet: ChangeSet, changes: ChangeSetChanges): Unit
}
