package kpn.server.monitor.repository

case class MonitorStateSummary(
  relationId: Long,
  deviationDistance: Long,
  deviationCount: Long,
)
