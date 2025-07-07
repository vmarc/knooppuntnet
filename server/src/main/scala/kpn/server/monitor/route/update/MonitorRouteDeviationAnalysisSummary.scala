package kpn.server.monitor.route.update

case class MonitorRouteDeviationAnalysisSummary(
  relationId: Long,
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
)
