package kpn.server.monitor.repository

case class MonitorRouteStateSummary(
  relationId: Long,
  deviationDistance: Long,
  deviationCount: Long,
)
