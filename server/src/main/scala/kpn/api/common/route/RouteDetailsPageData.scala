package kpn.api.common.route

import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.RouteSummary
import kpn.api.common.common.Reference
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.custom.Day
import kpn.api.custom.Timestamp

case class RouteDetailsPageData(
  id: Long,
  active: Boolean,
  summary: RouteSummary,
  proposed: Boolean,
  version: Long,
  changeSetId: Long,
  lastUpdated: Timestamp,
  lastSurvey: Option[Day],
  facts: Seq[Fact],
  locationCandidateInfos: Seq[LocationCandidateInfo],
  unexpectedNodeIds: Seq[Long],
  unexpectedRelationIds: Seq[Long],
  segments: Seq[RouteSegment],
  paths: Seq[RoutePath],
  structureRows: Seq[RouteStructureRow],
  nameDerivedFromNodes: Boolean,
  nodes: RouteNodes,
  bounds: Option[Bounds],
  routeIds: Seq[Long],
  parentRoutes: Seq[ParentRoute],
  networkReferences: Seq[Reference],
)
