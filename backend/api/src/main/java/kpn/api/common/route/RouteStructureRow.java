package kpn.api.common.route;

import kpn.api.common.data.MemberType;
import kpn.api.common.route.Link;
import kpn.api.common.route.RouteStructureRelation;
import kpn.api.common.route.RouteStructureWay;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteStructureRow(
  String rowNumber,
  Long level,
  Long id,
  MemberType memberType,
  Optional<String> role,
  Optional<Link> link,
  Long distance,
  Optional<String> name,
  Optional<String> poi,
  Optional<RouteStructureWay> way,
  Optional<RouteStructureRelation> relation,
  ImmutableList<Long> segmentIds,
  ImmutableList<Long> pathIds
) {}
