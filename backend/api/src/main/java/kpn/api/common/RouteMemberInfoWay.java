package kpn.api.common;

import kpn.api.common.route.Link;
import kpn.api.common.route.RouteNetworkNodeInfo;
import kpn.api.common.route.WayDirection;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteMemberInfoWay(
  Optional<String> wayType,
  ImmutableList<RouteNetworkNodeInfo> nodes,
  Timestamp timestamp,
  String surface,
  Boolean accessible,
  Boolean ferry,
  Long distance,
  String nodeCount,
  WayDirection oneWay,
  ImmutableList<Tag> oneWayTags,
  Link link
) {
}
