package kpn.api.common.search;

import kpn.api.common.Bounds;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteListItem(
  Long id,
  String name,
  Long distance,
  Optional<String> symbol,
  Optional<Bounds> bounds,
  ImmutableList<Long> routeIds
) {}
