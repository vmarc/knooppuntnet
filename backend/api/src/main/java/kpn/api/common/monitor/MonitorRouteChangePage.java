package kpn.api.common.monitor;

import kpn.api.common.Bounds;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.common.monitor.MonitorReferenceInfo;
import kpn.api.common.monitor.MonitorRouteDeviation;
import kpn.api.common.monitor.MonitorRouteSegment;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorRouteChangePage(
  ChangeKey key,
  String groupName,
  String groupDescription,
  Optional<String> comment,
  Long wayCount,
  Long waysAdded,
  Long waysRemoved,
  Long waysUpdated,
  Long osmDistance,
  Bounds bounds,
  Long routeSegmentCount,
  ImmutableList<MonitorRouteSegment> routeSegments,
  ImmutableList<MonitorRouteDeviation> newDeviations,
  ImmutableList<MonitorRouteDeviation> resolvedDeviations,
  MonitorReferenceInfo reference,
  Boolean happy,
  Boolean investigate
) {
}
