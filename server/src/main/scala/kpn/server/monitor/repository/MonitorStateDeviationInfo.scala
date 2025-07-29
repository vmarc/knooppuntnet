package kpn.server.monitor.repository

case class MonitorStateDeviationInfo(
  relationId: Long,
  deviationDistance: Long,
  deviationCount: Long,
)
