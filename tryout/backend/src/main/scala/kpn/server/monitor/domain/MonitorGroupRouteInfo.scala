package kpn.server.monitor.domain

import kpn.api.common.Bounds

case class MonitorGroupRouteInfo(
  groupId: String,
  monitorRouteIds: Seq[String],
  bounds: Option[Bounds]
)
