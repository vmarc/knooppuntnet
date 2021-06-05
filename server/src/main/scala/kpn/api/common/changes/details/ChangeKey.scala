package kpn.api.common.changes.details

import kpn.api.common.TimeKey
import kpn.api.custom.Timestamp

object ChangeKey {

  def apply(
    replicationNumber: Long,
    timestamp: Timestamp,
    changeSetId: Long,
    elementId: Long
  ): ChangeKey = {
    ChangeKey(
      replicationNumber,
      timestamp,
      changeSetId,
      elementId,
      timestamp.toKey
    )
  }
}

case class ChangeKey(
  replicationNumber: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  elementId: Long,
  time: TimeKey
)
