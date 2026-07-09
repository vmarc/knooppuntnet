package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp
import kpn.core.doc.Storable

case class LogAction(
  timestamp: ActionTimestamp,
  logfile: String,
  values: Seq[LogValue]
) extends Storable
