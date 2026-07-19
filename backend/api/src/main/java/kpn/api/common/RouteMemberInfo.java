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

/*
package kpn.api.common

import kpn.api.common.data.MemberType

case class RouteMemberInfo(
  id: Long,
  memberType: MemberType,
  role: Option[String],
  name: Option[String],
  poi: Option[String],
  way: Option[RouteMemberInfoWay],
  segmentIds: Seq[Long],
  pathIds: Seq[Long],
)

*/
