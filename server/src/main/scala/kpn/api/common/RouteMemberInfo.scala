package kpn.api.common

import kpn.api.common.data.MemberType

case class RouteMemberInfo(
  id: Long,
  memberType: MemberType,
  role: Option[String],
  name: Option[String],
  poi: Option[String],
  way: Option[RouteMemberInfoWay],
)
