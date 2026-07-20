package kpn.api.common.location;

public record LocationSummary(
  Long factCount,
  Long nodeCount,
  Long routeCount,
  Long changesCount
) {}
