package kpn.api.common.monitor

case class MonitorRouteChangesPage(
  routeId: Long,
  routeName: String,
  groupName: String,
  groupDescription: String,
  impact: Boolean,
  pageIndex: Long,
  itemsPerPage: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)
