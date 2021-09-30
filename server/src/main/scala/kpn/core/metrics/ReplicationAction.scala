package kpn.core.metrics

case class ReplicationAction(
  minuteDiff: MinuteDiffInfo,
  fileSize: Long,
  elementCount: Long,
  changeSetCount: Long
)
