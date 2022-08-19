package kpn.api.common.monitor

case class MonitorGroupsPage(
  adminRole: Boolean,
  routeCount: Long,
  groups: Seq[MonitorGroupsPageGroup]
)
