package kpn.server.monitor.domain

import kpn.core.doc.Storable

case class MonitorReferenceTile(
  z: Long,
  x: Long,
  y: Long,
  lines: Seq[String]
) extends Storable
