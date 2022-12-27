package kpn.api.common.monitor

import kpn.api.custom.Day

case class MonitorRouteDetail(
  rowIndex: Long,
  routeId: String,
  name: String,
  description: String,
  relationId: Option[Long],
  referenceType: String,
  referenceDay: Option[Day],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmSegmentCount: Long,
  happy: Boolean
)
