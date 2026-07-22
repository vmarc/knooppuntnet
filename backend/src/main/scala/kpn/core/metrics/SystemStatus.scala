package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp
import kpn.api.id.Storable

case class SystemStatus(
  timestamp: ActionTimestamp,
  values: Seq[SystemStatusValue]
) extends Storable
