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

  override def nodeDetails(language: Language, nodeId: Long): ApiResponse[NodeDetailsPage] = {
    api.execute("node-details", s"$nodeId") {
      reply(nodeDetailsPageBuilder.build(language, nodeId))
    }
  }

  override def nodeMap(nodeId: Long): ApiResponse[NodeMapPage] = {
    api.execute("node-map", s"$nodeId") {
      reply(nodeMapPageBuilder.build(nodeId))
    }
  }

  override def nodeChanges(nodeId: Long, parameters: ChangesParameters): ApiResponse[NodeChangesPage] = {
    api.execute("node-changes", s"node=$nodeId, ${parameters.toDisplayString}") {
      reply(nodeChangesPageBuilder.build(nodeId, parameters))
    }
  }

  override def routeDetails(language: Language, routeId: Long): ApiResponse[RouteDetailsPage] = {
    api.execute("route-details", s"$routeId") {
      reply(routeDetailsPageBuilder.build(language, routeId))
    }
  }

  override def routeMap(routeId: Long): ApiResponse[RouteMapPage] = {
    api.execute("route-map", s"$routeId") {
      reply(routeMapPageBuilder.build(routeId))
    }
  }

  override def routeChanges(routeId: Long, parameters: ChangesParameters): ApiResponse[RouteChangesPage] = {
    api.execute("route-changes", s"route=$routeId, ${parameters.toDisplayString}") {
      reply(routeChangesPageBuilder.build(routeId, parameters))
    }
  }

  override def networkDetails(networkId: Long): ApiResponse[NetworkDetailsPage] = {
    api.execute("network-details", s"$networkId") {
      reply(networkDetailsPageBuilder.build(networkId))
    }
  }

  override def networkMap(networkId: Long): ApiResponse[NetworkMapPage] = {
    api.execute("network-map", s"$networkId") {
      reply(networkMapPageBuilder.build(networkId))
    }
  }

  override def networkFacts(networkId: Long): ApiResponse[NetworkFactsPage] = {
    api.execute("network-facts", s"$networkId") {
      reply(networkFactsPageBuilder.build(networkId))
    }
  }

  override def networkNodes(networkId: Long): ApiResponse[NetworkNodesPage] = {
    api.execute("network-nodes", s"$networkId") {
      reply(networkNodesPageBuilder.build(networkId))
    }
  }

  override def networkRoutes(networkId: Long): ApiResponse[NetworkRoutesPage] = {
    api.execute("network-routes", s"$networkId") {
      reply(networkRoutesPageBuilder.build(networkId))
    }
  }

  override def networkChanges(networkId: Long, parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    api.execute("network-changes", s"networkId=$networkId, ${parameters.toDisplayString}") {
      reply(networkChangesPageBuilder.build(networkId, parameters))
    }
  }

  override def subsetNetworks(subset: Subset): ApiResponse[SubsetNetworksPage] = {
    api.execute("subset-networks", s"${subset.string}") {
      reply(Some(subsetNetworksPageBuilder.build(subset)))
    }
  }

  override def subsetFacts(subset: Subset): ApiResponse[SubsetFactsPage] = {
    api.execute("subset-facts", s"${subset.string}") {
      reply(Some(subsetFactsPageBuilder.build(subset)))
    }
  }

  override def subsetFactDetails(subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    api.execute("subset-fact-details", s"${subset.string}, ${fact.name}") {
      reply(Some(subsetFactDetailsPageBuilder.build(subset, fact)))
    }
  }

  override def subsetFactRefs(subset: Subset, fact: Fact): ApiResponse[SubsetFactRefs] = {
    api.execute("subset-fact-refs", s"${subset.string}, ${fact.name}") {
      reply(Some(subsetFactRefsBuilder.build(subset, fact)))
    }
  }

  override def subsetChanges(subset: Subset, parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    api.execute("subset-changes", s"subset=${subset.name}, ${parameters.toDisplayString}") {
      reply(subsetChangesPageBuilder.build(subset, parameters))
    }
  }

  override def subsetOrphanRoutes(subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    api.execute("subset-orphan-routes", subset.string) {
      reply(Some(subsetOrphanRoutesPageBuilder.build(subset)))
    }
  }

  override def subsetOrphanNodes(subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    api.execute("subset-orphan-nodes", subset.string) {
      reply(Some(subsetOrphanNodesPageBuilder.build(subset)))
    }
  }

  override def subsetMap(subset: Subset): ApiResponse[SubsetMapPage] = {
    api.execute("subset-map", subset.string) {
      reply(Some(subsetMapPageBuilder.build(subset)))
    }
  }

  override def overview(language: Language): ApiResponse[Seq[StatisticValues]] = {
    api.execute("overview", "") {
      reply(overviewPageBuilder.build(language))
    }
  }

  override def changeSet(language: Language, changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage] = {
    val args = s"changeSetId=$changeSetId, replicationId=${replicationId.map(_.name)}"
    api.execute("change-set", args) {
      reply(changeSetPageBuilder.build(language, changeSetId, replicationId))
    }
  }

  override def replication(language: Language, changeSetId: Long): ApiResponse[Long] = {
    val args = s"changeSetId=$changeSetId}"
    api.execute("change-set", args) {
      reply(changeSetPageBuilder.build(language, changeSetId, None).map(_.summary.key.replicationNumber))
    }
  }

  override def changes(language: Language, strategy: AnalysisStrategy, parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    api.execute("changes", parameters.toDisplayString) {
      reply(Some(changesPageBuilder.build(language, strategy, parameters)))
    }
  }

  override def locations(language: Language, networkType: NetworkType, country: Country): ApiResponse[LocationsPage] = {
    api.execute("location", networkType.name) {
      reply(locationsPageBuilder.build(language, networkType, country))
    }
  }

  override def locationNodes(language: Language, key: LocationKey, parameters: LocationNodesParameters): ApiResponse[LocationNodesPage] = {
    val locationKey = s"${key.networkType.name}, ${key.country.domain}, ${key.name}, "
    val locationParameters = s"${parameters.locationNodesType.name}, ${parameters.pageSize}, ${parameters.pageIndex}"
    api.execute("location-nodes", locationKey + locationParameters) {
      reply(locationNodesPageBuilder.build(language, key, parameters))
    }
  }

  override def locationRoutes(language: Language, locationKey: LocationKey, parameters: LocationRoutesParameters): ApiResponse[LocationRoutesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute("location-routes", args) {
      reply(locationRoutesPageBuilder.build(language, locationKey, parameters))
    }
  }

  override def locationFacts(language: Language, locationKey: LocationKey): ApiResponse[LocationFactsPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute("location-facts", args) {
      reply(locationFactsPageBuilder.build(language, locationKey))
    }
  }

  override def locationMap(language: Language, locationKey: LocationKey): ApiResponse[LocationMapPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute("location-map", args) {
      reply(locationMapPageBuilder.build(language, locationKey))
    }
  }

  override def locationChanges(language: Language, locationKey: LocationKey, parameters: LocationChangesParameters): ApiResponse[LocationChangesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute("location-changes", args) {
      reply(locationChangesPageBuilder.build(language, locationKey, parameters))
    }
  }

  override def locationEdit(language: Language, locationKey: LocationKey): ApiResponse[LocationEditPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute("location-edit", args) {
      reply(locationEditPageBuilder.build(language, locationKey))
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(analysisRepository.lastUpdated(), 1, result)
    TimestampLocal.localize(response)
    response
  }
}
