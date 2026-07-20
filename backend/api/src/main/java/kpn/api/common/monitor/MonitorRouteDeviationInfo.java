package kpn.api.common.monitor;

import kpn.api.common.Bounds;

public record MonitorRouteDeviationInfo(
  Long id,
  Long meters,
  Long distance,
  Bounds bounds
) {}
