package kpn.api.common.route;

import kpn.api.common.route.RouteNetworkNodeInfo;
import kpn.api.common.route.WayDirection;
import kpn.api.custom.Tag;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteStructureWay(
  Optional<String> wayType,
  ImmutableList<RouteNetworkNodeInfo> nodes,
  String surface,
  Boolean accessible,
  String nodeCount,
  WayDirection oneWay,
  ImmutableList<Tag> oneWayTags
) {
}
