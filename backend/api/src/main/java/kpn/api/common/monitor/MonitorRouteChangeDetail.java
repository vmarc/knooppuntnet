package kpn.api.common.monitor;

import kpn.api.common.Bounds;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.common.monitor.MonitorRouteDeviation;
import kpn.api.common.monitor.MonitorRouteSegment;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorRouteChangeDetail(
  ChangeKey key,
  Optional<String> comment,
  Long wayCount,
  Long waysAdded,
  Long waysRemoved,
  Long waysUpdated,
  Long osmDistance,
  Long gpxDistance,
  String gpxFilename,
  Bounds bounds,
  String referenceJson,
  Long routeSegmentCount,
  ImmutableList<MonitorRouteSegment> routeSegments,
  ImmutableList<MonitorRouteDeviation> newDeviations,
  ImmutableList<MonitorRouteDeviation> resolvedDeviations,
  Boolean happy,
  Boolean investigate
) {
}
