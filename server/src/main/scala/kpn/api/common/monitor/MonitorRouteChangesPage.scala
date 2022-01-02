package kpn.api.common.monitor

case class MonitorRouteChangesPage(
  routeId: Long,
  routeName: String,
  groupName: String,
  groupDescription: String,
  impact: Boolean,
  pageSize: Long,
  pageIndex: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)
