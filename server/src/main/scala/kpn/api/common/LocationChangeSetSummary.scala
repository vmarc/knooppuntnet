package kpn.api.common

import kpn.api.base.WithStringId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.common.ToStringBuilder
import kpn.api.custom.Timestamp

case class LocationChangeSetSummary(
  _id: String,
  key: ChangeKey,
  timestampFrom: Timestamp,
  timestampUntil: Timestamp,
  trees: Seq[LocationChangesTree],
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) extends WithStringId {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("_id", _id).
    field("key", key).
    field("timestampFrom", timestampFrom).
    field("timestampUntil", timestampUntil).
    field("trees", trees).
    field("happy", happy).
    field("investigate", investigate).
    field("impact", impact).
    build
}
