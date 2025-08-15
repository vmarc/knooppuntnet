package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp
import kpn.core.doc.Storable

case class SystemStatus(
  timestamp: ActionTimestamp,
  values: Seq[SystemStatusValue]
) extends Storable
