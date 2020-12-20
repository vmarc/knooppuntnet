package kpn.api.common.monitor

case class MonitorChangesParameters(
  itemsPerPage: Long = 5,
  pageIndex: Long = 0,
  impact: Boolean = false
)
