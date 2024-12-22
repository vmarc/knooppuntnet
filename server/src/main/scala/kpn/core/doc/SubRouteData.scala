package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.RouteMemberInfo

case class SubRouteData(
  _id: Long, // routeId
  name: String,
  members: Seq[RouteMemberInfo],
  distance: Long
) extends WithId {
}
