package kpn.api.common.monitor;

import kpn.api.common.Bounds;
import kpn.api.common.monitor.MonitorReferenceInfo;
import kpn.api.common.monitor.MonitorReferenceType;
import kpn.api.common.monitor.MonitorRouteDeviation;
import kpn.api.common.monitor.MonitorRouteSegment;
import kpn.api.common.monitor.MonitorRouteSubRelation;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorRouteMapPage(
  Optional<Long> relationId,
  String routeName,
  String routeDescription,
  String groupName,
  String groupDescription,
  MonitorReferenceType referenceType,
  Optional<Bounds> bounds,
  Optional<Timestamp> analysisTimestamp,
  Optional<MonitorRouteSubRelation> currentSubRelation,
  Optional<MonitorRouteSubRelation> previousSubRelation,
  Optional<MonitorRouteSubRelation> nextSubRelation,
  ImmutableList<MonitorRouteSegment> osmSegments,
  Optional<String> matchesGeoJson,
  ImmutableList<MonitorRouteDeviation> deviations,
  Optional<MonitorReferenceInfo> reference,
  ImmutableList<MonitorRouteSubRelation> subRelations
) {}
