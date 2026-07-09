package kpn.api.common.monitor

case class MonitorRouteDeviationsPage(
  summary: MonitorRouteSummary,
  deviationDistance: Long,
  deviations: Seq[MonitorRouteDeviationInfo]
)
