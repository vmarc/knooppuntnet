package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp

case class LogAction(
  timestamp: ActionTimestamp,
  logfile: String,
  values: Seq[LogValue]
)
