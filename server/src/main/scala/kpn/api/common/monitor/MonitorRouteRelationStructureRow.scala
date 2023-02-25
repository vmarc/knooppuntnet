package kpn.api.common.monitor

import kpn.api.custom.Day

case class MonitorRouteRelationStructureRow(
  level: Long,
  physical: Boolean,
  name: String,
  relationId: Long,
  role: Option[String],
  survey: Option[Day],
  referenceDay: Option[Day],
  referenceFilename: Option[String],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
  osmSegmentCount: Long,
  happy: Boolean
)
