package kpn.api.common

import kpn.api.common.data.MemberType

case class RouteMemberInfo(
  id: Long,
  memberType: MemberType,
  role: String,
  way: Option[RouteMemberInfoWay]
)
