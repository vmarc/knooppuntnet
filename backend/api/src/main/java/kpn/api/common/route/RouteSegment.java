package kpn.api.common.route;

import kpn.api.common.Bounds;

import com.google.common.collect.ImmutableList;

public record RouteSegment(
  Long id,
  Long startNodeId,
  Long endNodeId,
  Long meters,
  Bounds bounds,
  ImmutableList<Long> elementIds
) {
}

/*
package kpn.api.common.route

import kpn.api.common.Bounds

case class RouteSegment(
  id: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  bounds: Bounds,
  elementIds: Seq[Long]
)

*/
