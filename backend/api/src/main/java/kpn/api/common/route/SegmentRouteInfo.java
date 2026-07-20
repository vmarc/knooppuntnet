package kpn.api.common.route;

import com.google.common.collect.ImmutableList;

public record SegmentRouteInfo(
  Long relationId,
  ImmutableList<Long> segmentIds
) {
}

