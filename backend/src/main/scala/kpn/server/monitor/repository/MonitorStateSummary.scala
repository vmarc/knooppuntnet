package kpn.server.monitor.repository

import kpn.api.id.Storable

case class MonitorStateSummary(
  relationId: Long,
  deviationDistance: Long,
  deviationCount: Long,
) extends Storable
