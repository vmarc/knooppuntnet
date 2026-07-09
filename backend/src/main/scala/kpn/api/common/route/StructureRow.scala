package kpn.api.common.route

import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class StructureRow(
  rowNumber: String,
  level: Long,
  id: Long,
  memberType: MemberType,
  role: Option[String],
  link: Option[Link],
  distance: Long,
  name: Option[String],
  poi: Option[String],
  way: Option[RouteStructureWay],
  relation: Option[RouteStructureRelation],
  segmentIds: Seq[Long],
  pathIds: Seq[Long],

  physical: Boolean,
  relationId: Long, // from MonitorRouteRelationStructureRow also included in 'id' when MemberType.Relation
  subRelationIndex: Option[Long], // from MonitorRouteRelationStructureRow OBSOLETE?
  survey: Option[Day],
  symbol: Option[String],
  referenceType: Option[MonitorReferenceType],
  referenceTimestamp: Option[Timestamp],
  referenceFilename: Option[String],
  referenceDistance: Long,
  deviationDistance: Option[Long],
  deviationCount: Option[Long],
  osmSegmentCount: Option[Long], // from MonitorRouteRelationStructureRow, should match segmentIds.length ???
  osmDistance: Long, // from MonitorRouteRelationStructureRow, should match 'distance' ???
  osmDistanceSubRelations: Long, // from MonitorRouteRelationStructureRow, matches RouteStructureRelation.totalDistance
  gaps: Option[String],
  showMap: Boolean, // from MonitorRouteRelationStructureRow OBSOLETE?
  happy: Boolean
)
