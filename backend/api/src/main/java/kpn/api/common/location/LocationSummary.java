package kpn.api.common.location;

public record LocationSummary(
  Long factCount,
  Long nodeCount,
  Long routeCount,
  Long changesCount
) {
}

/*
package kpn.api.common.location

case class LocationSummary(
  factCount: Long,
  nodeCount: Long,
  routeCount: Long,
  changesCount: Long
)

*/
