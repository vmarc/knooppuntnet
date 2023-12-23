package kpn.api.common.monitor

import kpn.api.custom.Timestamp

case class MonitorRouteDetail(
  rowIndex: Long,
  name: String,
  description: String,
  symbol: Option[String],
  relationId: Option[Long],
  referenceType: String,
  referenceTimestamp: Option[Timestamp],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmSegmentCount: Long,
  happy: Boolean
)
