package kpn.api.common.route;

import com.google.common.collect.ImmutableList;

public record SegmentRouteInfo(
  Long relationId,
  ImmutableList<Long> segmentIds
) {
}

/*
package kpn.api.common.route

case class SegmentRouteInfo(
  relationId: Long, // routeId in route tiles
  segmentIds: Seq[Long]
)

*/
