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
  symbol: Option[String],
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceDistance: Long,
  deviationDistance: Option[Long],
  deviationCount: Option[Long],
  osmSegmentCount: Option[Long],
  osmDistance: Long,
  osmDistanceSubRelations: Long,
  gaps: Option[String],
  showMap: Boolean,
  happy: Boolean
)
