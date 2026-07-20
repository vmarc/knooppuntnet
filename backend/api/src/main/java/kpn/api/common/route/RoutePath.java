package kpn.api.common.route;

import com.google.common.collect.ImmutableList;

public record RoutePath(
  Long id,
  String name,
  ImmutableList<Long> elementIds
) {
}
