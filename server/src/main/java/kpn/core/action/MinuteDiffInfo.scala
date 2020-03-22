package kpn.core.action

case class MinuteDiffInfo(
  id: Long,
  timestamp: ActionTimestamp,
  processed: ActionTimestamp,
  delay: Long
)
