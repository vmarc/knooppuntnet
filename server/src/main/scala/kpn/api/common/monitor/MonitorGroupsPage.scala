package kpn.api.common.monitor

case class MonitorGroupsPage(
  adminUser: Boolean,
  routeCount: Long,
  groups: Seq[MonitorGroupsPageGroup]
)
