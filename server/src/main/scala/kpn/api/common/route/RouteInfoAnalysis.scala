package kpn.api.common.route

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.common.ToStringBuilder
import kpn.api.custom.RouteMemberInfo

case class RouteInfoAnalysis(
  unexpectedNodeIds: Seq[Long],
  members: Seq[RouteMemberInfo],
  expectedName: String,
  map: RouteMap,
  structureStrings: Seq[String],
  geometryDigest: String,
  locationAnalysis: Option[RouteLocationAnalysis]
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("unexpectedNodeIds", unexpectedNodeIds).
    field("members", members).
    field("expectedName", expectedName).
    field("map", map).
    field("structureStrings", structureStrings).
    field("geometryDigest", geometryDigest).
    field("locationAnalysis", locationAnalysis).
    build
}
