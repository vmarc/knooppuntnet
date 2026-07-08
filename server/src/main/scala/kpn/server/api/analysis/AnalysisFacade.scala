package kpn.server.api.analysis

import kpn.api.common.AnalysisStrategy
import kpn.api.common.ChangesPage
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.Language
import kpn.api.common.ReplicationId
import kpn.api.common.RouteType
import kpn.api.common.SearchResponse
import kpn.api.common.changes.ChangeSetPage
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.location.LocationChangesPage
import kpn.api.common.location.LocationDetailsPage
import kpn.api.common.location.LocationEditPage
import kpn.api.common.location.LocationFactsPage
import kpn.api.common.location.LocationMapPage
import kpn.api.common.location.LocationNodesPage
import kpn.api.common.location.LocationNodesParameters
import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationRoutesParameters
import kpn.api.common.location.LocationsPage
import kpn.api.common.network.NetworkChangesPage
import kpn.api.common.network.NetworkDetailsPage
import kpn.api.common.network.NetworkFactsPage
import kpn.api.common.network.NetworkMapPage
import kpn.api.common.network.NetworkNodesPage
import kpn.api.common.network.NetworkRoutesPage
import kpn.api.common.node.NodeChangesPage
import kpn.api.common.node.NodeDetailsPage
import kpn.api.common.route.RouteChangesPage
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteMembersPage
import kpn.api.common.route.RoutePathsPage
import kpn.api.common.route.RouteSegmentsPage
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteList
import kpn.api.common.statistics.OverviewPage
import kpn.api.common.subset.SubsetChangesPage
import kpn.api.common.subset.SubsetFactDetailsPage
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.common.subset.SubsetFactsPage
import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.custom.ApiResponse
import kpn.api.custom.LocationKey
import kpn.api.custom.Subset
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.api.analysis.pages.ChangeSetPageBuilder
import kpn.server.api.analysis.pages.ChangesPageBuilder
import kpn.server.api.analysis.pages.LocationsPageBuilder
import kpn.server.api.analysis.pages.OverviewPageBuilder
import kpn.server.api.analysis.pages.location.LocationChangesPageBuilder
import kpn.server.api.analysis.pages.location.LocationDetailsPageBuilder
import kpn.server.api.analysis.pages.location.LocationEditPageBuilder
import kpn.server.api.analysis.pages.location.LocationFactsPageBuilder
import kpn.server.api.analysis.pages.location.LocationMapPageBuilder
import kpn.server.api.analysis.pages.location.LocationNodesPageBuilder
import kpn.server.api.analysis.pages.location.LocationRoutesPageBuilder
import kpn.server.api.analysis.pages.network.NetworkChangesPageBuilder
import kpn.server.api.analysis.pages.network.NetworkDetailsPageBuilder
import kpn.server.api.analysis.pages.network.NetworkFactsPageBuilder
import kpn.server.api.analysis.pages.network.NetworkMapPageBuilder
import kpn.server.api.analysis.pages.network.NetworkNodesPageBuilder
import kpn.server.api.analysis.pages.network.NetworkRoutesPageBuilder
import kpn.server.api.analysis.pages.node.NodeChangesPageBuilder
import kpn.server.api.analysis.pages.node.NodeDetailsPageBuilder
import kpn.server.api.analysis.pages.route.RouteChangesPageBuilder
import kpn.server.api.analysis.pages.route.RouteDetailsPageBuilder
import kpn.server.api.analysis.pages.route.RouteMembersPageBuilder
import kpn.server.api.analysis.pages.route.RoutePathsPageBuilder
import kpn.server.api.analysis.pages.route.RouteSegmentsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetChangesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactDetailsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactRefsBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetNetworksPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanNodesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanRoutesPageBuilder
import kpn.server.repository.AnalysisRepository
import kpn.server.search.SearchFacade
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class AnalysisFacade(
  api: Api,
  analysisRepository: AnalysisRepository,
  // ---
  overviewPageBuilder: OverviewPageBuilder,
  nodeDetailsPageBuilder: NodeDetailsPageBuilder,
  nodeChangesPageBuilder: NodeChangesPageBuilder,
  routeDetailsPageBuilder: RouteDetailsPageBuilder,
  routeMembersPageBuilder: RouteMembersPageBuilder,
  routePathsPageBuilder: RoutePathsPageBuilder,
  routeSegmentsPageBuilder: RouteSegmentsPageBuilder,
  routeChangesPageBuilder: RouteChangesPageBuilder,
  networkDetailsPageBuilder: NetworkDetailsPageBuilder,
  networkMapPageBuilder: NetworkMapPageBuilder,
  networkFactsPageBuilder: NetworkFactsPageBuilder,
  networkNodesPageBuilder: NetworkNodesPageBuilder,
  networkRoutesPageBuilder: NetworkRoutesPageBuilder,
  subsetNetworksPageBuilder: SubsetNetworksPageBuilder,
  subsetFactsPageBuilder: SubsetFactsPageBuilder,
  subsetFactDetailsPageBuilder: SubsetFactDetailsPageBuilder,
  subsetFactRefsBuilder: SubsetFactRefsBuilder,
  subsetChangesPageBuilder: SubsetChangesPageBuilder,
  subsetOrphanRoutesPageBuilder: SubsetOrphanRoutesPageBuilder,
  subsetOrphanNodesPageBuilder: SubsetOrphanNodesPageBuilder,
  changesPageBuilder: ChangesPageBuilder,
  changeSetPageBuilder: ChangeSetPageBuilder,
  networkChangesPageBuilder: NetworkChangesPageBuilder,
  locationsPageBuilder: LocationsPageBuilder,
  locationEditPageBuilder: LocationEditPageBuilder,
  locationDetailsPageBuilder: LocationDetailsPageBuilder,
  locationNodesPageBuilder: LocationNodesPageBuilder,
  locationRoutesPageBuilder: LocationRoutesPageBuilder,
  locationFactsPageBuilder: LocationFactsPageBuilder,
  locationMapPageBuilder: LocationMapPageBuilder,
  locationChangesPageBuilder: LocationChangesPageBuilder,
  searchFacade: SearchFacade
) {

  def nodeDetails(language: Language, nodeId: Long): ApiResponse[NodeDetailsPage] = {
    api.execute("node-details", s"$nodeId") {
      reply(nodeDetailsPageBuilder.build(language, nodeId))
    }
  }

  def nodeChanges(nodeId: Long, parameters: ChangesParameters): ApiResponse[NodeChangesPage] = {
    api.execute("node-changes", s"node=$nodeId, ${parameters.toDisplayString}") {
      reply(nodeChangesPageBuilder.build(nodeId, parameters))
    }
  }

  def routeDetails(language: Language, routeId: Long): ApiResponse[RouteDetailsPage] = {
    api.execute("route-details", s"$routeId") {
      reply(routeDetailsPageBuilder.build(language, routeId))
    }
  }

  def routeMembers(language: Language, routeId: Long): ApiResponse[RouteMembersPage] = {
    api.execute("route-members", s"$routeId") {
      reply(routeMembersPageBuilder.build(language, routeId))
    }
  }

  def routePaths(language: Language, routeId: Long): ApiResponse[RoutePathsPage] = {
    api.execute("route-paths", s"$routeId") {
      reply(routePathsPageBuilder.build(language, routeId))
    }
  }

  def routeSegments(language: Language, routeId: Long): ApiResponse[RouteSegmentsPage] = {
    api.execute("route-segments", s"$routeId") {
      reply(routeSegmentsPageBuilder.build(language, routeId))
    }
  }

  def routeChanges(routeId: Long, parameters: ChangesParameters): ApiResponse[RouteChangesPage] = {
    api.execute("route-changes", s"route=$routeId, ${parameters.toDisplayString}") {
      reply(routeChangesPageBuilder.build(routeId, parameters))
    }
  }

  def networkDetails(networkId: Long): ApiResponse[NetworkDetailsPage] = {
    api.execute("network-details", s"$networkId") {
      reply(networkDetailsPageBuilder.build(networkId))
    }
  }

  def networkMap(networkId: Long): ApiResponse[NetworkMapPage] = {
    api.execute("network-map", s"$networkId") {
      reply(networkMapPageBuilder.build(networkId))
    }
  }

  def networkFacts(networkId: Long): ApiResponse[NetworkFactsPage] = {
    api.execute("network-facts", s"$networkId") {
      reply(networkFactsPageBuilder.build(networkId))
    }
  }

  def networkNodes(networkId: Long): ApiResponse[NetworkNodesPage] = {
    api.execute("network-nodes", s"$networkId") {
      reply(networkNodesPageBuilder.build(networkId))
    }
  }

  def networkRoutes(networkId: Long): ApiResponse[NetworkRoutesPage] = {
    api.execute("network-routes", s"$networkId") {
      reply(networkRoutesPageBuilder.build(networkId))
    }
  }

  def networkChanges(networkId: Long, parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    api.execute("network-changes", s"networkId=$networkId, ${parameters.toDisplayString}") {
      reply(networkChangesPageBuilder.build(networkId, parameters))
    }
  }

  def subsetNetworks(subset: Subset): ApiResponse[SubsetNetworksPage] = {
    api.execute("subset-networks", s"${subset.string}") {
      reply(Some(subsetNetworksPageBuilder.build(subset)))
    }
  }

  def subsetFacts(subset: Subset): ApiResponse[SubsetFactsPage] = {
    api.execute("subset-facts", s"${subset.string}") {
      reply(Some(subsetFactsPageBuilder.build(subset)))
    }
  }

  def subsetFactDetails(subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    api.execute("subset-fact-details", s"${subset.string}, ${fact.entryName}") {
      reply(Some(subsetFactDetailsPageBuilder.build(subset, fact)))
    }
  }

  def subsetFactRefs(subset: Subset, fact: Fact): ApiResponse[SubsetFactRefs] = {
    api.execute("subset-fact-refs", s"${subset.string}, ${fact.entryName}") {
      reply(Some(subsetFactRefsBuilder.build(subset, fact)))
    }
  }

  def subsetChanges(subset: Subset, parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    api.execute("subset-changes", s"subset=${subset.name}, ${parameters.toDisplayString}") {
      reply(subsetChangesPageBuilder.build(subset, parameters))
    }
  }

  def subsetOrphanRoutes(subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    api.execute("subset-orphan-routes", subset.string) {
      reply(Some(subsetOrphanRoutesPageBuilder.build(subset)))
    }
  }

  def subsetOrphanNodes(subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    api.execute("subset-orphan-nodes", subset.string) {
      reply(Some(subsetOrphanNodesPageBuilder.build(subset)))
    }
  }

  def overview(language: Language): ApiResponse[OverviewPage] = {
    api.execute("overview", "") {
      reply(overviewPageBuilder.build(language))
    }
  }

  def changeSet(language: Language, changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage] = {
    val args = s"changeSetId=$changeSetId, replicationId=${replicationId.map(_.name)}"
    api.execute("change-set", args) {
      reply(changeSetPageBuilder.build(language, changeSetId, replicationId))
    }
  }

  def changes(language: Language, strategy: AnalysisStrategy, parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    api.execute("changes", parameters.toDisplayString) {
      reply(Some(changesPageBuilder.build(language, strategy, parameters)))
    }
  }

  def locations(language: Language, routeType: RouteType, country: Country): ApiResponse[LocationsPage] = {
    val subset = Subset.of(country, routeType).get
    api.execute("location", routeType.entryName) {
      reply(locationsPageBuilder.build(language, subset))
    }
  }

  def locationDetails(language: Language, locationKey: LocationKey): ApiResponse[LocationDetailsPage] = {
    val args = s"${locationKey.routeType.entryName}, ${locationKey.country.entryName}, ${locationKey.name}"
    api.execute("location-details", args) {
      reply(locationDetailsPageBuilder.build(language, locationKey))
    }
  }

  def locationNodes(language: Language, key: LocationKey, parameters: LocationNodesParameters): ApiResponse[LocationNodesPage] = {
    val args = Seq(
      Some(key.routeType.entryName),
      Some(key.country.entryName),
      Some(key.name),
      Some(s"pageSize=${parameters.pageSize}"),
      Some(s"pageIndex=${parameters.pageIndex}"),
      parameters.integrityCheck.map(v => s"integrityCheck=${v.toString}"),
      parameters.integrityCheckFailed.map(v => s"integrityCheckFailed=${v.toString}"),
      parameters.fact.map(v => s"fact=${v.toString}"),
      parameters.survey.map(v => s"survey=${v.toString}"),
      parameters.lastUpdated.map(v => s"lastUpdated=${v.toString}"),
      parameters.proposed.map(v => s"proposed=${v.toString}"),
      parameters.referencedInRoutes.map(v => s"referencedInRoutes=${v.toString}"),
    ).flatten.mkString(", ")

    api.execute("location-nodes", args) {
      reply(locationNodesPageBuilder.build(language, key, parameters))
    }
  }

  def locationRoutes(language: Language, locationKey: LocationKey, parameters: LocationRoutesParameters): ApiResponse[LocationRoutesPage] = {
    val args = Seq(
      Some(locationKey.routeType.entryName),
      Some(locationKey.country.entryName),
      Some(locationKey.name),
      Some(s"pageSize=${parameters.pageSize}"),
      Some(s"pageIndex=${parameters.pageIndex}"),
      parameters.fact.map(v => s"fact=${v.toString}"),
      parameters.survey.map(v => s"survey=${v.toString}"),
      parameters.lastUpdated.map(v => s"lastUpdated=${v.toString}"),
      parameters.proposed.map(v => s"proposed=${v.toString}"),
    ).flatten.mkString(", ")

    api.execute("location-routes", args) {
      reply(locationRoutesPageBuilder.build(language, locationKey, parameters))
    }
  }

  def locationFacts(language: Language, locationKey: LocationKey): ApiResponse[LocationFactsPage] = {
    val args = s"${locationKey.routeType.entryName}, ${locationKey.country.entryName}, ${locationKey.name}"
    api.execute("location-facts", args) {
      reply(locationFactsPageBuilder.build(language, locationKey))
    }
  }

  def locationMap(language: Language, locationKey: LocationKey): ApiResponse[LocationMapPage] = {
    val args = s"${locationKey.routeType.entryName}, ${locationKey.country.entryName}, ${locationKey.name}"
    api.execute("location-map", args) {
      reply(locationMapPageBuilder.build(language, locationKey))
    }
  }

  def locationChanges(language: Language, locationKey: LocationKey, parameters: ChangesParameters): ApiResponse[LocationChangesPage] = {
    val args = s"${locationKey.routeType.entryName}, ${locationKey.country.entryName}, ${locationKey.name}"
    api.execute("location-changes", args) {
      reply(locationChangesPageBuilder.build(language, locationKey, parameters))
    }
  }

  def locationEdit(language: Language, locationKey: LocationKey): ApiResponse[LocationEditPage] = {
    val args = s"${locationKey.routeType.entryName}, ${locationKey.country.entryName}, ${locationKey.name}"
    api.execute("location-edit", args) {
      reply(locationEditPageBuilder.build(language, locationKey))
    }
  }

  def search(query: String): ApiResponse[SearchResponse] = {
    val args = s"query=$query"
    api.execute("search", args) {
      reply(searchFacade.search(query))
    }
  }

  def explore(query: ConditionGroup): ApiResponse[RouteList] = {
    val args = s"query=$query"
    api.execute("explore", args) {
      reply(Some(searchFacade.explore(query)))
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(analysisRepository.lastUpdated(), 1, result)
    TimestampLocal.localize(response)
    response
  }
}
