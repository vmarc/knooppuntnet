package kpn.api.common.monitor

import kpn.api.common.Bounds

case class MonitorRouteSummary(
  adminUser: Boolean,
  groupName: String,
  routeName: String,
  routeDescription: String,
  routeId: String,
  relationId: Option[Long],
  relationIds: Seq[Long],
  memberCount: Long,
  segmentCount: Long,
  deviationCount: Long,
  bounds: Option[Bounds]
)
