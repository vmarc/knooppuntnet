package kpn.api.common.route

import kpn.api.common.Bounds
import kpn.api.common.Fact
import kpn.api.common.RouteSummary
import kpn.api.common.common.Reference
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.database.actions.routes.RouteDetailsData

object RouteDetails {
  def from(routeDetailsData: RouteDetailsData, locationCandidateInfos: Seq[LocationCandidateInfo]): RouteDetails = {
    RouteDetails(
      routeDetailsData.id,
      routeDetailsData.active,
      routeDetailsData.summary,
      routeDetailsData.proposed,
      routeDetailsData.version,
      routeDetailsData.changeSetId,
      routeDetailsData.lastUpdated,
      routeDetailsData.lastSurvey,
      routeDetailsData.facts,
      routeDetailsData.unexpectedNodeIds,
      routeDetailsData.unexpectedRelationIds,
      routeDetailsData.memberCount,
      routeDetailsData.segmentCount,
      routeDetailsData.pathCount,
      routeDetailsData.nameDerivedFromNodes,
      routeDetailsData.nodes,
      routeDetailsData.bounds,
      routeDetailsData.routeIds,
      routeDetailsData.relationCount,
      routeDetailsData.relationLevels,
      routeDetailsData.parentRoutes,
      routeDetailsData.networkReferences,
      locationCandidateInfos,
    )
  }
}

case class RouteDetails(
  id: Long,
  active: Boolean,
  summary: RouteSummary,
  proposed: Boolean,
  version: Long,
  changeSetId: Long,
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
  locationCandidateInfos: Seq[LocationCandidateInfo],
)
