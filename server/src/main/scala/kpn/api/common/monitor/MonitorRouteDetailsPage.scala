package kpn.api.common.monitor

import kpn.api.custom.Day

case class MonitorRouteDetailsPage(
  adminRole: Boolean,
  groupName: String,
  groupDescription: String,
  routeName: String,
  routeDescription: String,
  relationId: Option[Long],
  comment: Option[String],
  referenceType: String,
  referenceDay: Option[Day],
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
