package kpn.api.common.route

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.common.ToStringBuilder

case class RouteInfoAnalysis(
  startNodes: Seq[RouteNetworkNodeInfo],
  endNodes: Seq[RouteNetworkNodeInfo],
  startTentacleNodes: Seq[RouteNetworkNodeInfo],
  endTentacleNodes: Seq[RouteNetworkNodeInfo],
  unexpectedNodeIds: Seq[Long],
  members: Seq[RouteMemberInfo],
  expectedName: String,
  map: RouteMap,
  structureStrings: Seq[String],
  locationAnalysis: Option[RouteLocationAnalysis]
){

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("startNodes", startNodes).
    field("endNodes", endNodes).
    field("startTentacleNodes", startTentacleNodes).
    field("endTentacleNodes", endTentacleNodes).
    field("unexpectedNodeIds", unexpectedNodeIds).
    field("members", members).
    field("expectedName", expectedName).
    field("map", map).
    field("structureStrings", structureStrings).
    build
}
