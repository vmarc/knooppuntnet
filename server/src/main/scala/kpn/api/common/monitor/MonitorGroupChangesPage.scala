package kpn.api.common.monitor

case class MonitorGroupChangesPage(
  groupName: String,
  groupDescription: String,
  impact: Boolean,
  pageIndex: Long,
  itemsPerPage: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)
