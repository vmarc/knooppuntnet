package kpn.api.common.common;

import com.google.common.collect.ImmutableSet;

public record KnownElements(
  ImmutableSet<Long> nodeIds,
  ImmutableSet<Long> routeIds
) {
}

/*
package kpn.api.common.common

case class KnownElements(
  nodeIds: Set[Long] = Set.empty,
  routeIds: Set[Long] = Set.empty
)

*/
