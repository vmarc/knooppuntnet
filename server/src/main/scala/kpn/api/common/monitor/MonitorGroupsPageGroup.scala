package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorGroupsPageGroup(
  id: String,
  name: String,
  description: String,
  routeCount: Long,
  monitorRouteIds: Seq[String],
  bounds: Option[Bounds]
)
