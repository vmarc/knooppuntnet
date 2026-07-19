package kpn.api.common.route;

import kpn.api.common.Bounds;

import com.google.common.collect.ImmutableList;

public record BaseRouteSegment(
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

case class BaseRouteSegment(
  id: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  bounds: Bounds,
  elementIds: Seq[Long]
)

*/
