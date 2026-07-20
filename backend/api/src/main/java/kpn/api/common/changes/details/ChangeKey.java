package kpn.api.common.changes.details;

import kpn.api.common.TimeKey;
import kpn.api.custom.Timestamp;

public record ChangeKey(
  Long replicationNumber,
  Timestamp timestamp,
  Long changeSetId,
  Long elementId,
  TimeKey time
) {

  public String toId() {
    return changeSetId + ":" + replicationNumber + ":" + elementId;
  }

  public String toShortId() {
    return changeSetId + ":" + replicationNumber;
  }
}

/* TODO migrate

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

*/
