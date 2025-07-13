package kpn.server.monitor.domain

case class MonitorReferenceTile(
  z: Long,
  x: Long,
  y: Long,
  lines: Seq[String]
)
