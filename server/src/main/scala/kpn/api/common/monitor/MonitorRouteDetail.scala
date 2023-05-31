package kpn.api.common.monitor

import kpn.api.custom.Timestamp

case class MonitorRouteDetail(
  rowIndex: Long,
  routeId: String,
  name: String,
  description: String,
  relationId: Option[Long],
  referenceType: String,
  referenceTimestamp: Option[Timestamp],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmSegmentCount: Long,
  happy: Boolean
)
