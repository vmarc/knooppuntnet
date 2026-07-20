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
  Long relationId, // from MonitorRouteRelationStructureRow also included in 'id' when MemberType.Relation
  Optional<Long> subRelationIndex, // from MonitorRouteRelationStructureRow OBSOLETE?
  Optional<Day> survey,
  Optional<String> symbol,
  Optional<MonitorReferenceType> referenceType,
  Optional<Timestamp> referenceTimestamp,
  Optional<String> referenceFilename,
  Long referenceDistance,
  Optional<Long> deviationDistance,
  Optional<Long> deviationCount,
  Optional<Long> osmSegmentCount, // from MonitorRouteRelationStructureRow, should match segmentIds.length ???
  Long osmDistance, // from MonitorRouteRelationStructureRow, should match 'distance' ???
  Long osmDistanceSubRelations, // from MonitorRouteRelationStructureRow, matches RouteStructureRelation.totalDistance
  Optional<String> gaps,
  Boolean showMap, // from MonitorRouteRelationStructureRow OBSOLETE?
  Boolean happy
) {
}
