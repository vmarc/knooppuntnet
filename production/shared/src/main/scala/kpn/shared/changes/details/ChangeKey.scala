package kpn.shared.changes.details

import kpn.shared.Timestamp

case class ChangeKey(
  replicationNumber: Int,
  timestamp: Timestamp,
  changeSetId: Long,
  elementId: Long
)
