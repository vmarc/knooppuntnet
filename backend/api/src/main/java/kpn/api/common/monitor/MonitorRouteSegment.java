package kpn.api.common.monitor;

import kpn.api.common.Bounds;

public record MonitorRouteSegment(
  Long id,
  Long startNodeId,
  Long endNodeId,
  Long meters,
  Bounds bounds,
  String geoJson
) {}

