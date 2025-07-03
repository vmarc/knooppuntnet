package kpn.api.common.route

import kpn.api.common.data.MemberType
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class StructureRow(
  rowNumber: String,
  level: Long, // from MonitorRouteRelationStructureRow also included in RouteStructureRelation.level
  id: Long, // from RouteStructureRow
  memberType: MemberType, // from RouteStructureRow
  role: Option[String], // from RouteStructureRow and  MonitorRouteRelationStructureRow
  link: Option[Link], // from RouteStructureRow
  distance: Long, // from RouteStructureRow
  name: Option[String], // from RouteStructureRow and as non-option in MonitorRouteRelationStructureRow
  poi: Option[String], // from RouteStructureRow
  way: Option[RouteStructureWay], // from RouteStructureRow
  relation: Option[RouteStructureRelation], // from RouteStructureRow
  segmentIds: Seq[Long], // from RouteStructureRow
  pathIds: Seq[Long], // from RouteStructureRow

  physical: Boolean, // from MonitorRouteRelationStructureRow
  relationId: Long, // from MonitorRouteRelationStructureRow also included in 'id' when MemberType.Relation
  subRelationIndex: Option[Long], // from MonitorRouteRelationStructureRow OBSOLETE?
  survey: Option[Day], // from MonitorRouteRelationStructureRow
  symbol: Option[String], // from MonitorRouteRelationStructureRow
  referenceTimestamp: Option[Timestamp], // from MonitorRouteRelationStructureRow
  referenceFilename: Option[String], // from MonitorRouteRelationStructureRow
  referenceDistance: Long, // from MonitorRouteRelationStructureRow
  deviationDistance: Option[Long], // from MonitorRouteRelationStructureRow
  deviationCount: Option[Long], // from MonitorRouteRelationStructureRow
  osmSegmentCount: Option[Long], // from MonitorRouteRelationStructureRow, should match segmentIds.length ???
  osmDistance: Long, // from MonitorRouteRelationStructureRow, should match 'distance' ???
  osmDistanceSubRelations: Long, // from MonitorRouteRelationStructureRow, matches RouteStructureRelation.totalDistance
  gaps: Option[String], // from MonitorRouteRelationStructureRow
  showMap: Boolean, // from MonitorRouteRelationStructureRow OBSOLETE?
  happy: Boolean // from MonitorRouteRelationStructureRow
)
