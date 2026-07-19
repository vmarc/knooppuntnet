package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorGroupPage(
  adminUser: Boolean,
  groupId: String,
  groupName: String,
  groupDescription: String,
  bounds: Option[Bounds],
  relationIds: Seq[Long],
  routes: Seq[MonitorRouteDetail]
)
