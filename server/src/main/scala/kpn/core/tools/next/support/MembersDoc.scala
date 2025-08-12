package kpn.core.tools.next.support

import kpn.api.common.RouteMemberInfo
import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class MembersDoc(
  _id: Long,
  members: Seq[RouteMemberInfo],
) extends Storable
