package kpn.api.common

import kpn.api.base.WithStringId
import kpn.api.common.changes.details.ChangeKey
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
}
