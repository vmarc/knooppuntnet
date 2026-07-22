package kpn.api.common.changes.details;

import kpn.api.common.TimeKey;
import kpn.api.custom.Timestamp;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ChangeKey(
  @NonNull Long replicationNumber,
  @NonNull Timestamp timestamp,
  @NonNull Long changeSetId,
  @NonNull Long elementId
  // @NonNull TimeKey time // TODO migrate - can eliminate ???
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
