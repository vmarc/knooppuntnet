package kpn.api.common.route

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.raw.Raw
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.custom.Day
import kpn.api.custom.Timestamp
import kpn.core.analysis.Facts
import kpn.database.actions.routes.RouteDetailsData

object RouteDetails {
  def from(routeDetailsData: RouteDetailsData, locationCandidateInfos: Seq[LocationCandidateInfo]): RouteDetails = {
    val broken = routeDetailsData.facts.exists(Facts.isError)
    val incomplete = routeDetailsData.facts.contains(Fact.RouteIncomplete)
    RouteDetails(
      routeDetailsData.id,
      routeDetailsData.active,
      routeDetailsData.raw,
      routeDetailsData.countries,
      routeDetailsData.nodeNetwork,
      routeDetailsData.routeTypes,
      routeDetailsData.scopes,
      routeDetailsData.name,
      routeDetailsData.meters,
      routeDetailsData.wayCount,
      broken,
      incomplete,
      routeDetailsData.proposed,
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
  raw: Raw,
  countries: Seq[Country],
  nodeNetwork: Boolean,
  routeTypes: Seq[RouteType],
  scopes: Seq[RouteScope],
  name: String,
  meters: Long,
  wayCount: Long,
  broken: Boolean,
  incomplete: Boolean,
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
  locationCandidateInfos: Seq[LocationCandidateInfo],
)
