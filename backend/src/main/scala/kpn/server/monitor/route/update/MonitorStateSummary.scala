package kpn.server.monitor.route.update

case class MonitorStateSummary(
  relationId: Long,
  deviationCount: Long,
  deviationDistance: Long,
  matchesDistance: Long,
)
