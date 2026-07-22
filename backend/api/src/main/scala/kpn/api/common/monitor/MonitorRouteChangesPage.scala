package kpn.api.common.monitor

case class MonitorRouteChangesPage(
  routeId: String,
  routeName: String,
  groupName: String,
  groupDescription: String,
  impact: Boolean,
  pageSize: Long,
  pageIndex: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)
