package kpn.server.monitor.domain

import kpn.core.doc.Storable

case class MonitorStateTileDeviation(
  id: Long,
  lines: Seq[String]
) extends Storable
