package kpn.api.common;

import kpn.api.common.RouteMemberInfoWay;
import kpn.api.common.data.MemberType;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteMemberInfo(
  Long id,
  MemberType memberType,
  Optional<String> role,
  Optional<String> name,
  Optional<String> poi,
  Optional<RouteMemberInfoWay> way,
  ImmutableList<Long> segmentIds,
  ImmutableList<Long> pathIds
) {
}
