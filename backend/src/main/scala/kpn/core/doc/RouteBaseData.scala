package kpn.core.doc

import kpn.api.common.Country
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.data.raw.Raw
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteNodes
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.api.id.Storable

case class RouteBaseData(
  raw: Raw,
  countries: Seq[Country],
  nodeNetwork: Boolean,
  routeTypes: Seq[RouteType],
  scopes: Seq[RouteScope],
  // TODO redesign - reintroduce routeScope: RouteScope, ?
  name: String,
  meters: Long,
  wayCount: Long,
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
