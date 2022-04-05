package kpn.api.common.monitor

case class MonitorGroupPage(
  adminRole: Boolean,
  groupName: String,
  groupDescription: String,
  routes: Seq[MonitorRouteDetail]
)
