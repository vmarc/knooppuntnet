package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.ChangeKeyI
import kpn.api.custom.Timestamp

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

  def buildChangeKeyI(elementId: Long): ChangeKeyI = {
    ChangeKeyI(
      replicationId.number,
      changeSet.timestamp.yyyymmddhhmmss,
      changeSet.id,
      elementId
    )
  }
}
