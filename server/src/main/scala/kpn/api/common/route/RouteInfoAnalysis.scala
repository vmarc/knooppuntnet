package kpn.api.common.route

import kpn.api.common.RouteLocationAnalysis
import kpn.api.custom.RouteMemberInfo

case class RouteInfoAnalysis(
  unexpectedNodeIds: Seq[Long],
  members: Seq[RouteMemberInfo],
  expectedName: String,
  nameDerivedFromNodes: Boolean,
  map: RouteMap,
  structureStrings: Seq[String],
  geometryDigest: String,
  locationAnalysis: RouteLocationAnalysis
)
