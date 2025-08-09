package kpn.core.tools.next.support

import kpn.api.common.RouteMemberInfo

// TODO scala3 move back into using class
case class MembersDoc(
  _id: Long,
  members: Seq[RouteMemberInfo],
)
