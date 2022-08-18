package kpn.api.common.monitor

case class MonitorGroupPage(
  groupId: String,
  groupName: String,
  groupDescription: String,
  adminRole: Boolean,
  routes: Seq[MonitorRouteDetail]
)
