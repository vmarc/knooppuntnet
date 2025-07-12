package kpn.server.monitor.route.update

case class MonitorRouteStateSummary(
  relationId: Long,
  deviationCount: Long,
  deviationDistance: Long,
  matchesDistance: Long,
)
