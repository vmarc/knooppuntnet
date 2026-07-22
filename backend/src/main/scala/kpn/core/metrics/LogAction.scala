package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp
import kpn.api.id.Storable

case class LogAction(
  timestamp: ActionTimestamp,
  logfile: String,
  values: Seq[LogValue]
) extends Storable
