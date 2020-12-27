package kpn.api.common.monitor

case class MonitorChangesPage(
  impact: Boolean,
  pageIndex: Long,
  itemsPerPage: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)
