package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp

case class SystemStatus(
  timestamp: ActionTimestamp,
  values: Seq[SystemStatusValue]
)
