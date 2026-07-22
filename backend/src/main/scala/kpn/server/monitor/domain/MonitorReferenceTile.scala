package kpn.server.monitor.domain

import kpn.api.id.Storable

case class MonitorReferenceTile(
  z: Long,
  x: Long,
  y: Long,
  lines: Seq[String]
) extends Storable
