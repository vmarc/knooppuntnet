package kpn.core.action

import kpn.api.common.status.ActionTimestamp

case class LogAction(
  timestamp: ActionTimestamp,
  logfile: String,
  values: Seq[LogValue]
)
