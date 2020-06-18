package kpn.core.action

import kpn.api.common.status.ActionTimestamp

case class MinuteDiffInfo(
  id: Long,
  timestamp: ActionTimestamp,
  processed: ActionTimestamp,
  delay: Long
)
