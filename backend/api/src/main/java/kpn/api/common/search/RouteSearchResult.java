package kpn.api.common.search;

import kpn.api.common.Bounds;
import kpn.api.common.RouteScope;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteSearchResult(
  Long id,
  String name,
  ImmutableList<RouteScope> scopes,
  Long distance,
  Optional<String> symbol,
  Optional<Bounds> bounds,
  ImmutableList<Long> routeIds
) {
}

/* TODO migrate

  def toRouteListItem: RouteListItem = {
    RouteListItem(
      id,
      name,
      distance,
      symbol,
      bounds,
      routeIds,
    )
  }

*/
