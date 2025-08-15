package kpn.core.metrics

import kpn.api.common.status.ActionTimestamp
import kpn.core.doc.Storable

case class MinuteDiffInfo(
  id: Long,
  timestamp: ActionTimestamp,
  processed: ActionTimestamp,
  delay: Long
) extends Storable
