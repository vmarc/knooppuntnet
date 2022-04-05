package kpn.api.common.monitor

case class MonitorGroupsPage(
  adminRole: Boolean,
  groups: Seq[MonitorGroupDetail]
)
