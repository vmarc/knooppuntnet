package kpn.api.common.route;

import kpn.api.common.Bounds;
import kpn.api.common.Country;
import kpn.api.common.Fact;
import kpn.api.common.RouteScope;
import kpn.api.common.RouteType;
import kpn.api.common.common.Reference;
import kpn.api.common.data.raw.Raw;
import kpn.api.common.location.LocationCandidateInfo;
import kpn.api.common.route.ParentRoute;
import kpn.api.common.route.RouteNodes;
import kpn.api.custom.Day;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteDetails(
  Long id,
  Boolean active,
  Raw raw,
  ImmutableList<Country> countries,
  Boolean nodeNetwork,
  ImmutableList<RouteType> routeTypes,
  ImmutableList<RouteScope> scopes,
  String name,
  Long meters,
  Long wayCount,
  Boolean broken,
  Boolean incomplete,
  Boolean proposed,
  Timestamp lastUpdated,
  Optional<Day> lastSurvey,
  ImmutableList<Fact> facts,
  ImmutableList<Long> unexpectedNodeIds,
  ImmutableList<Long> unexpectedRelationIds,
  Long memberCount,
  Long segmentCount,
  Long pathCount,
  Boolean nameDerivedFromNodes,
  RouteNodes nodes,
  Optional<Bounds> bounds,
  ImmutableList<Long> routeIds,
  Long relationCount,
  Long relationLevels,
  ImmutableList<ParentRoute> parentRoutes,
  ImmutableList<Reference> networkReferences,
  ImmutableList<LocationCandidateInfo> locationCandidateInfos
) {}

/* TODO migrate

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

*/
