package kpn.api.common

import kpn.api.common.data.MemberType

case class RouteMemberInfo(
  id: Long,
  memberType: MemberType,
  role: String,
  linkName: String,
  way: Option[RouteMemberInfoWay]
)
