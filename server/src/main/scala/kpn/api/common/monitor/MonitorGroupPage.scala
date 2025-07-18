package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorGroupPage(
  groupId: String,
  groupName: String,
  groupDescription: String,
  adminRole: Boolean,
  bounds: Option[Bounds],
  relationIds: Seq[Long],
  routes: Seq[MonitorRouteDetail]
)
