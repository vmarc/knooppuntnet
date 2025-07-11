package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.RouteMemberInfo
import kpn.api.common.route.BaseRouteSegment

case class SubRouteData(
  _id: Long, // routeId
  name: String,
  members: Seq[RouteMemberInfo],
  distance: Long,
  segments: Seq[BaseRouteSegment],
) extends WithId
