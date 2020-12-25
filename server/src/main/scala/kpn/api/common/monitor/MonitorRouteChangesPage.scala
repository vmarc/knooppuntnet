package kpn.api.common.monitor

case class MonitorRouteChangesPage(
  routeId: Long,
  routeName: String,
  groupName: String,
  groupDescription: String,
  changes: Seq[MonitorRouteChangeSummary]
)
