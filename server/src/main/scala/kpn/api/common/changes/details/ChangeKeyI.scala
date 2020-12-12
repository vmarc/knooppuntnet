package kpn.api.common.changes.details

case class ChangeKeyI(
  replicationNumber: Long,
  timestamp: String,
  changeSetId: Long,
  elementId: Long
)