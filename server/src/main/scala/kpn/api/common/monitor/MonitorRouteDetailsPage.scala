package kpn.api.common.monitor

import kpn.api.custom.Timestamp

case class MonitorRouteDetailsPage(
  adminRole: Boolean,
  groupName: String,
  groupDescription: String,
  routeName: String,
  routeDescription: String,
  relationId: Option[Long],
  comment: Option[String],
  symbol: Option[String],
  analysisTimestamp: Option[Timestamp],
  analysisDuration: Option[Long],
  referenceType: String,
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmSegmentCount: Long,
  happy: Boolean,
  wayCount: Long,
  osmDistance: Long,
  structureRows: Option[Seq[MonitorRouteRelationStructureRow]]
)
