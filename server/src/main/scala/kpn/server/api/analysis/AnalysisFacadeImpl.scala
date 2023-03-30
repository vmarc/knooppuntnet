package kpn.server.api.analysis

import kpn.api.common.AnalysisStrategy
import kpn.api.common.ChangesPage
import kpn.api.common.Language
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetPage
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.location.LocationChangesPage
import kpn.api.common.location.LocationChangesParameters
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
import kpn.api.common.node.NodeMapPage
import kpn.api.common.route.RouteChangesPage
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteMapPage
import kpn.api.common.statistics.StatisticValues
import kpn.api.common.subset.SubsetChangesPage
import kpn.api.common.subset.SubsetFactDetailsPage
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.common.subset.SubsetFactsPage
import kpn.api.common.subset.SubsetMapPage
import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.custom.ApiResponse
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.common.TimestampLocal
import kpn.server.api.Api
import kpn.server.api.analysis.pages.ChangeSetPageBuilder
import kpn.server.api.analysis.pages.ChangesPageBuilder
import kpn.server.api.analysis.pages.LocationsPageBuilder
import kpn.server.api.analysis.pages.OverviewPageBuilder
import kpn.server.api.analysis.pages.location.LocationChangesPageBuilder
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
import kpn.server.api.analysis.pages.node.NodeMapPageBuilder
import kpn.server.api.analysis.pages.route.RouteChangesPageBuilder
import kpn.server.api.analysis.pages.route.RouteDetailsPageBuilder
import kpn.server.api.analysis.pages.route.RouteMapPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetChangesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactDetailsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactRefsBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetMapPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetNetworksPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanNodesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanRoutesPageBuilder
import kpn.server.repository.AnalysisRepository
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest

@Component
class AnalysisFacadeImpl(
  api: Api,
  analysisRepository: AnalysisRepository,
  // ---
  overviewPageBuilder: OverviewPageBuilder,
  nodeDetailsPageBuilder: NodeDetailsPageBuilder,
  nodeMapPageBuilder: NodeMapPageBuilder,
  nodeChangesPageBuilder: NodeChangesPageBuilder,
  routeDetailsPageBuilder: RouteDetailsPageBuilder,
  routeMapPageBuilder: RouteMapPageBuilder,
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
  subsetMapPageBuilder: SubsetMapPageBuilder,
  changesPageBuilder: ChangesPageBuilder,
  changeSetPageBuilder: ChangeSetPageBuilder,
  networkChangesPageBuilder: NetworkChangesPageBuilder,
  locationsPageBuilder: LocationsPageBuilder,
  locationEditPageBuilder: LocationEditPageBuilder,
  locationNodesPageBuilder: LocationNodesPageBuilder,
  locationRoutesPageBuilder: LocationRoutesPageBuilder,
  locationFactsPageBuilder: LocationFactsPageBuilder,
  locationMapPageBuilder: LocationMapPageBuilder,
  locationChangesPageBuilder: LocationChangesPageBuilder
) extends AnalysisFacade {

  override def nodeDetails(request: HttpServletRequest, user: Option[String], language: Language, nodeId: Long): ApiResponse[NodeDetailsPage] = {
    api.execute(request, user, "node-details", s"$nodeId") {
      reply(nodeDetailsPageBuilder.build(user, language, nodeId))
    }
  }

  override def nodeMap(request: HttpServletRequest, user: Option[String], nodeId: Long): ApiResponse[NodeMapPage] = {
    api.execute(request, user, "node-map", s"$nodeId") {
      reply(nodeMapPageBuilder.build(user, nodeId))
    }
  }

  override def nodeChanges(request: HttpServletRequest, user: Option[String], nodeId: Long, parameters: ChangesParameters): ApiResponse[NodeChangesPage] = {
    api.execute(request, user, "node-changes", s"node=$nodeId, ${parameters.toDisplayString}") {
      reply(nodeChangesPageBuilder.build(user, nodeId, parameters))
    }
  }

  override def routeDetails(request: HttpServletRequest, user: Option[String], language: Language, routeId: Long): ApiResponse[RouteDetailsPage] = {
    api.execute(request, user, "route-details", s"$routeId") {
      reply(routeDetailsPageBuilder.build(user, language, routeId))
    }
  }

  override def routeMap(request: HttpServletRequest, user: Option[String], routeId: Long): ApiResponse[RouteMapPage] = {
    api.execute(request, user, "route-map", s"$routeId") {
      reply(routeMapPageBuilder.build(user, routeId))
    }
  }

  override def routeChanges(request: HttpServletRequest, user: Option[String], routeId: Long, parameters: ChangesParameters): ApiResponse[RouteChangesPage] = {
    api.execute(request, user, "route-changes", s"route=$routeId, ${parameters.toDisplayString}") {
      reply(routeChangesPageBuilder.build(user, routeId, parameters))
    }
  }

  override def networkDetails(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkDetailsPage] = {
    api.execute(request, user, "network-details", s"$networkId") {
      reply(networkDetailsPageBuilder.build(networkId))
    }
  }

  override def networkMap(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkMapPage] = {
    api.execute(request, user, "network-map", s"$networkId") {
      reply(networkMapPageBuilder.build(networkId))
    }
  }

  override def networkFacts(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkFactsPage] = {
    api.execute(request, user, "network-facts", s"$networkId") {
      reply(networkFactsPageBuilder.build(networkId))
    }
  }

  override def networkNodes(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkNodesPage] = {
    api.execute(request: HttpServletRequest, user, "network-nodes", s"$networkId") {
      reply(networkNodesPageBuilder.build(networkId))
    }
  }

  override def networkRoutes(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkRoutesPage] = {
    api.execute(request, user, "network-routes", s"$networkId") {
      reply(networkRoutesPageBuilder.build(networkId))
    }
  }

  override def networkChanges(request: HttpServletRequest, user: Option[String], networkId: Long, parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    api.execute(request, user, "network-changes", s"networkId=$networkId, ${parameters.toDisplayString}") {
      reply(networkChangesPageBuilder.build(user, networkId, parameters))
    }
  }

  override def subsetNetworks(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetNetworksPage] = {
    api.execute(request, user, "subset-networks", s"${subset.string}") {
      reply(Some(subsetNetworksPageBuilder.build(subset)))
    }
  }

  override def subsetFacts(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetFactsPage] = {
    api.execute(request, user, "subset-facts", s"${subset.string}") {
      reply(Some(subsetFactsPageBuilder.build(subset)))
    }
  }

  override def subsetFactDetails(request: HttpServletRequest, user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    api.execute(request, user, "subset-fact-details", s"${subset.string}, ${fact.name}") {
      reply(Some(subsetFactDetailsPageBuilder.build(subset, fact)))
    }
  }

  override def subsetFactRefs(request: HttpServletRequest, user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactRefs] = {
    api.execute(request, user, "subset-fact-refs", s"${subset.string}, ${fact.name}") {
      reply(Some(subsetFactRefsBuilder.build(subset, fact)))
    }
  }

  override def subsetChanges(request: HttpServletRequest, user: Option[String], subset: Subset, parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    api.execute(request, user, "subset-changes", s"subset=${subset.name}, ${parameters.toDisplayString}") {
      reply(subsetChangesPageBuilder.build(user, subset, parameters))
    }
  }

  override def subsetOrphanRoutes(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    api.execute(request, user, "subset-orphan-routes", subset.string) {
      reply(Some(subsetOrphanRoutesPageBuilder.build(subset)))
    }
  }

  override def subsetOrphanNodes(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    api.execute(request, user, "subset-orphan-nodes", subset.string) {
      reply(Some(subsetOrphanNodesPageBuilder.build(subset)))
    }
  }

  override def subsetMap(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetMapPage] = {
    api.execute(request, user, "subset-map", subset.string) {
      reply(Some(subsetMapPageBuilder.build(subset)))
    }
  }

  override def overview(request: HttpServletRequest, user: Option[String], language: Language): ApiResponse[Seq[StatisticValues]] = {
    api.execute(request, user, "overview", "") {
      reply(overviewPageBuilder.build(language))
    }
  }

  override def changeSet(request: HttpServletRequest, user: Option[String], language: Language, changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage] = {
    val args = s"changeSetId=$changeSetId, replicationId=${replicationId.map(_.name)}"
    api.execute(request, user, "change-set", args) {
      reply(changeSetPageBuilder.build(user, language, changeSetId, replicationId))
    }
  }

  override def replication(request: HttpServletRequest, user: Option[String], language: Language, changeSetId: Long): ApiResponse[Long] = {
    val args = s"changeSetId=$changeSetId}"
    api.execute(request, user, "change-set", args) {
      reply(changeSetPageBuilder.build(Some("app"), language, changeSetId, None).map(_.summary.key.replicationNumber))
    }
  }

  override def changes(request: HttpServletRequest, user: Option[String], language: Language, strategy: AnalysisStrategy, parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    api.execute(request, user, "changes", parameters.toDisplayString) {
      reply(Some(changesPageBuilder.build(user, language, strategy, parameters)))
    }
  }

  override def locations(request: HttpServletRequest, user: Option[String], language: Language, networkType: NetworkType, country: Country): ApiResponse[LocationsPage] = {
    api.execute(request, user, "location", networkType.name) {
      reply(locationsPageBuilder.build(language, networkType, country))
    }
  }

  override def locationNodes(request: HttpServletRequest, user: Option[String], language: Language, key: LocationKey, parameters: LocationNodesParameters): ApiResponse[LocationNodesPage] = {
    val locationKey = s"${key.networkType.name}, ${key.country.domain}, ${key.name}, "
    val locationParameters = s"${parameters.locationNodesType.name}, ${parameters.pageSize}, ${parameters.pageIndex}"
    api.execute(request, user, "location-nodes", locationKey + locationParameters) {
      reply(locationNodesPageBuilder.build(language, key, parameters))
    }
  }

  override def locationRoutes(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey, parameters: LocationRoutesParameters): ApiResponse[LocationRoutesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(request, user, "location-routes", args) {
      reply(locationRoutesPageBuilder.build(language, locationKey, parameters))
    }
  }

  override def locationFacts(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey): ApiResponse[LocationFactsPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(request, user, "location-facts", args) {
      reply(locationFactsPageBuilder.build(language, locationKey))
    }
  }

  override def locationMap(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey): ApiResponse[LocationMapPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(request, user, "location-map", args) {
      reply(locationMapPageBuilder.build(language, locationKey))
    }
  }

  override def locationChanges(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey, parameters: LocationChangesParameters): ApiResponse[LocationChangesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(request, user, "location-changes", args) {
      reply(locationChangesPageBuilder.build(language, locationKey, parameters))
    }
  }

  override def locationEdit(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey): ApiResponse[LocationEditPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(request, user, "location-edit", args) {
      reply(locationEditPageBuilder.build(language, locationKey))
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(analysisRepository.lastUpdated(), 1, result)
    TimestampLocal.localize(response)
    response
  }
}
