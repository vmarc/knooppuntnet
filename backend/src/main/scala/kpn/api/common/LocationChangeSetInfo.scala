package kpn.api.common

import kpn.api.common.changes.details.ChangeKey

case class LocationChangeSetInfo(
  rowIndex: Long,
  key: ChangeKey,
  comment: Option[String],
  happy: Boolean,
  investigate: Boolean,
  locationChanges: Seq[LocationChangesInfo]
)
