package kpn.server.monitor.domain

import kpn.api.id.Storable

case class MonitorStateTileDeviation(
  id: Long,
  lines: Seq[String]
) extends Storable
