package kpn.api.common.monitor

case class MonitorGroupPage(
  groupName: String,
  groupDescription: String,
  routes: Seq[MonitorRouteDetail]
)
