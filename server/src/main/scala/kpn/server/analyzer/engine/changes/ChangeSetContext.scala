package kpn.server.analyzer.engine.changes

import kpn.shared.ReplicationId
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeSet
import kpn.shared.changes.details.ChangeKey

case class ChangeSetContext(
  replicationId: ReplicationId,
  changeSet: ChangeSet
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
