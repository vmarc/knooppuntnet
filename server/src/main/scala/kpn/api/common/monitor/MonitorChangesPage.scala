package kpn.api.common.monitor

case class MonitorChangesPage(
  changes: Seq[MonitorRouteChangeSummary]
)
