package kpn.server.monitor.repository

import kpn.api.id.Storable

case class MonitorStateDeviationInfo(
  relationId: Long,
  deviationDistance: Long,
  deviationCount: Long,
) extends Storable
