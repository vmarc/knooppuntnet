package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.ElementIds

case class ChangeSetContext(
  replicationId: ReplicationId,
  changeSet: ChangeSet,
  elementIds: ElementIds,
  changes: ChangeSetChanges = ChangeSetChanges(),
  elementChanges: ElementChangeMap = new ElementChangeMap()
) {

  def timestampBefore: Timestamp = changeSet.timestampBefore

  def timestampAfter: Timestamp = changeSet.timestampAfter

  def buildChangeKey(elementId: Long): ChangeKey = {
    ChangeKey(
      replicationId.number,
      changeSet.timestamp,
      changeSet.id,
      elementId
    )
  }
}
