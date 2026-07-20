package kpn.api.common.route;

import kpn.api.common.route.BaseRouteSegment;
import kpn.api.custom.Day;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteStructureRelation(
  Boolean physical,
  String name,
  Optional<Long> subRelationIndex,
  Optional<Day> survey,
  Optional<String> symbol,
  ImmutableList<BaseRouteSegment> segments,
  Long totalDistance,
  Optional<String> gaps,
  Boolean happy
) {
}
