package kpn.api.common.common;

import com.google.common.collect.ImmutableSet;

public record KnownElements(
  ImmutableSet<Long> nodeIds,
  ImmutableSet<Long> routeIds
) {
}
