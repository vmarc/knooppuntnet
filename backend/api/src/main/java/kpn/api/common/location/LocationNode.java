package kpn.api.common.location;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record LocationNode(
  String name,
  Long nodeCount,
  Long routeCount,
  Long factCount,
  ImmutableList<LocationNode> children
) {
}
