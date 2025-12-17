package kpn.core.doc

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteSummary
import kpn.api.common.data.raw.Raw
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteNodes
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class RouteBaseData(
  raw: Raw,
  summary: RouteSummary,
  proposed: Boolean,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  unexpectedNodeIds: Seq[Long],
  members: Seq[RouteMemberInfo],
  nameDerivedFromNodes: Boolean,
  nodes: RouteNodes,
  analysis: RouteInfoAnalysis,
  locationAnalysis: RouteLocationAnalysis,
  networkNodeIds: Option[Seq[Long]],
  edges: Seq[RouteEdge],
) extends Storable
