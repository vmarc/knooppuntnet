package kpn.api.common.route;

public record RouteEdge(
  Long pathId,
  Long sourceNodeId,
  Long sinkNodeId,
  Long meters
) {
}

/*
package kpn.api.common.route

case class RouteEdge(
  pathId: Long,
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long
)

*/
