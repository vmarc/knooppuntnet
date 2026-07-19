package kpn.api.common.route;

import kpn.api.common.data.MemberType;
import kpn.api.common.monitor.MonitorReferenceType;
import kpn.api.common.route.Link;
import kpn.api.common.route.RouteStructureRelation;
import kpn.api.common.route.RouteStructureWay;
import kpn.api.custom.Day;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record StructureRow(
  String rowNumber,
  Long level,
  Long id,
  MemberType memberType,
  Optional<String> role,
  Optional<Link> link,
  Long distance,
  Optional<String> name,
  Optional<String> poi,
  Optional<RouteStructureWay> way,
  Optional<RouteStructureRelation> relation,
  ImmutableList<Long> segmentIds,
  ImmutableList<Long> pathIds,
  Boolean physical,
  Long relationId,
  Optional<Long> subRelationIndex,
  Optional<Day> survey,
  Optional<String> symbol,
  Optional<MonitorReferenceType> referenceType,
  Optional<Timestamp> referenceTimestamp,
  Optional<String> referenceFilename,
  Long referenceDistance,
  Optional<Long> deviationDistance,
  Optional<Long> deviationCount,
  Optional<Long> osmSegmentCount,
  Long osmDistance,
  Long osmDistanceSubRelations,
  Optional<String> gaps,
  Boolean showMap,
  Boolean happy
) {
}

/*
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

*/
