package kpn.api.common.monitor

case class MonitorGroupChangesPage(
  groupName: String,
  groupDescription: String,
  impact: Boolean,
  pageSize: Long,
  pageIndex: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)
