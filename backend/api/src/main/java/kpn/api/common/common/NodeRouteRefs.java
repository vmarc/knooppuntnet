package kpn.api.common.common;

import kpn.api.common.common.Reference;

import com.google.common.collect.ImmutableList;

public record NodeRouteRefs(
  Long nodeId,
  ImmutableList<Reference> routeRefs
) {
}
