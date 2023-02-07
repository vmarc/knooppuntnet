package kpn.server.monitor.repository

case class MonitorRouteStateSummary(
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
)
