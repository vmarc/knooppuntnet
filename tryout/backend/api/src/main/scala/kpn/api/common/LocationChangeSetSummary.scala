package kpn.api.common

import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.Timestamp
import kpn.core.doc.WithStringId

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
