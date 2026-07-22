package kpn.server.monitor.repository

import kpn.core.doc.Storable

case class MonitorStateSummary(
  relationId: Long,
  deviationDistance: Long,
  deviationCount: Long,
) extends Storable
