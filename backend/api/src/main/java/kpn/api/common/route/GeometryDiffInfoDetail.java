package kpn.api.common.route;

public record GeometryDiffInfoDetail(
  Long wayCount,
  Long nodeCount,
  Long meters
) {
}

/*
package kpn.api.common.route

case class GeometryDiffInfoDetail(
  wayCount: Long,
  nodeCount: Long,
  meters: Long
)

*/
