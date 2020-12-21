package kpn.api.common.monitor

case class MonitorGroupChangesPage(
  groupName: String,
  groupDescription: String,
  changes: Seq[MonitorRouteChangeSummary]
)
