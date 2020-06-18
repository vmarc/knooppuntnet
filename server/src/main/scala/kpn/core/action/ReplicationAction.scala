package kpn.core.action

case class ReplicationAction(
  minuteDiff: MinuteDiffInfo,
  fileSize: Long,
  elementCount: Long,
  changeSetCount: Long
)
