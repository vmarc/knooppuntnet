package kpn.server.repository

case class MonitorRouteStateSummary(
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
)
