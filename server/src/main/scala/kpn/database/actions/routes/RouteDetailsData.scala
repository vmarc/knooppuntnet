package kpn.database.actions.routes

import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteSummary
import kpn.api.common.common.Reference
import kpn.api.common.data.raw.Raw
import kpn.api.common.route.ParentRoute
import kpn.api.common.route.RouteNodes
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.doc.Storable

case class RouteDetailsData(
  id: Long,
  active: Boolean,
  core: Raw,
  summary: RouteSummary,
  proposed: Boolean,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  unexpectedNodeIds: Seq[Long],
  unexpectedRelationIds: Seq[Long],
  memberCount: Long,
  segmentCount: Long,
  pathCount: Long,
  nameDerivedFromNodes: Boolean,
  nodes: RouteNodes,
  bounds: Option[Bounds],
  routeIds: Seq[Long],
  relationCount: Long,
  relationLevels: Long,
  parentRoutes: Seq[ParentRoute],
  networkReferences: Seq[Reference],
  locationAnalysis: RouteLocationAnalysis,
) extends Storable
