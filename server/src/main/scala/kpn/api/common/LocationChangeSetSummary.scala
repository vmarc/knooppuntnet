package kpn.api.common

import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.common.ToStringBuilder
import kpn.api.custom.Timestamp

case class LocationChangeSetSummary(
  key: ChangeKey,
  timestampFrom: Timestamp,
  timestampUntil: Timestamp,
  trees: Seq[LocationChangesTree],
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("key", key).
    field("timestampFrom", timestampFrom).
    field("timestampUntil", timestampUntil).
    field("trees", trees).
    field("happy", happy).
    field("investigate", investigate).
    field("impact", impact).
    build
}
