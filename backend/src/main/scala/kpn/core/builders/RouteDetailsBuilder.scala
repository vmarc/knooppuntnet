package kpn.core.builders

import kpn.api.common.Fact
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.common.route.RouteDetails
import kpn.core.analysis.Facts
import kpn.database.actions.routes.RouteDetailsData

object RouteDetailsBuilder {
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
