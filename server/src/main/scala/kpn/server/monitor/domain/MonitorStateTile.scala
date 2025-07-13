package kpn.server.monitor.domain

case class MonitorStateTile(
  z: Long,
  x: Long,
  y: Long,
  deviations: Seq[MonitorStateTileDeviation],
  matchesLines: Seq[String],
)
