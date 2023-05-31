package kpn.api.common.monitor

import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class MonitorRouteRelationStructureRow(
  level: Long,
  physical: Boolean,
  name: String,
  relationId: Long,
  role: Option[String],
  survey: Option[Day],
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
  osmSegmentCount: Long,
  happy: Boolean
)
