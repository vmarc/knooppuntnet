package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.doc.Storable

object MinuteDiffInfo {
  def from(id: Long, timestamp: Timestamp): MinuteDiffInfo = {
    val localTimestamp = timestamp.toLocal
    val localNow = Time.now.toLocal
    val delay = (localNow.toInstant.toEpochMilli - localTimestamp.toInstant.toEpochMilli) / 1000

    MinuteDiffInfo(
      id,
      ActionTimestamp.fromZoned(localTimestamp),
      ActionTimestamp.fromZoned(localNow),
      delay
    )
  }
}

case class MinuteDiffInfo(
  id: Long,
  timestamp: ActionTimestamp,
  processed: ActionTimestamp,
  delay: Long
) extends Storable
