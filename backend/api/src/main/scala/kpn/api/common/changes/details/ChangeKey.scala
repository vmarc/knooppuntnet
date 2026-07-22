package kpn.api.common.changes.details

import kpn.api.common.TimeKey
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil

object ChangeKey {

  def apply(
    replicationNumber: Long,
    timestamp: Timestamp,
    changeSetId: Long,
    elementId: Long
  ): ChangeKey = {
    val key = TimestampUtil.toKey(timestamp)
    ChangeKey(
      replicationNumber,
      timestamp,
      changeSetId,
      elementId,
      key
    )
  }
}

case class ChangeKey(
  replicationNumber: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  elementId: Long,
  time: TimeKey
) {
  def toId: String = s"$changeSetId:$replicationNumber:$elementId"

  def toShortId: String = s"$changeSetId:$replicationNumber"
}
