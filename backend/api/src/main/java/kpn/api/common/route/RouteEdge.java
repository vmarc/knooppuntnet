package kpn.api.common.route;

public record RouteEdge(
  Long pathId,
  Long sourceNodeId,
  Long sinkNodeId,
  Long meters
) {}
