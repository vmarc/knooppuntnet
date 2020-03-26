package kpn.core.action

import kpn.api.common.status.ActionTimestamp

case class SystemStatus(
  timestamp: ActionTimestamp,
  values: Seq[SystemStatusValue]
)
