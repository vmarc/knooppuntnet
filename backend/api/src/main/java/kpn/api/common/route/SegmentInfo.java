package kpn.api.common.route;

import kpn.api.common.Bounds;
import kpn.api.common.route.SegmentRouteInfo;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record SegmentInfo(
  Long id,
  Long meters,
  Optional<Bounds> bounds,
  ImmutableList<SegmentRouteInfo> routeInfos
) {
}

/*
package kpn.api.common.route

import kpn.api.common.Bounds

case class SegmentInfo(
  id: Long,
  meters: Long,
  bounds: Option[Bounds],
  routeInfos: Seq[SegmentRouteInfo]
)

*/
