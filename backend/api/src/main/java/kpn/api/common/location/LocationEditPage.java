package kpn.api.common.location;

import kpn.api.common.Bounds;
import kpn.api.common.TimeInfo;
import kpn.api.common.location.LocationSummary;

import com.google.common.collect.ImmutableList;

public record LocationEditPage(
  TimeInfo timeInfo,
  LocationSummary summary,
  Boolean tooManyNodes,
  Long maxNodes,
  Bounds bounds,
  ImmutableList<Long> nodeIds,
  ImmutableList<Long> routeIds
) {
}

