package kpn.server.api.analysis

import kpn.api.common.ChangesPage
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
import kpn.core.gpx.GpxFile
import kpn.server.api.Api
import kpn.server.api.analysis.pages.ChangeSetPageBuilder
import kpn.server.api.analysis.pages.ChangesPageBuilder
import kpn.server.api.analysis.pages.LocationsPageBuilder
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
import kpn.server.api.analysis.pages.route.RoutePageBuilder
import kpn.server.api.analysis.pages.subset.SubsetChangesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactDetailsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetMapPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetNetworksPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanNodesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanRoutesPageBuilder
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.StatisticsRepository
import org.springframework.stereotype.Component

@Component
class AnalysisFacadeImpl(
  api: Api,
  networkRepository: NetworkRepository,
  overviewRepository: StatisticsRepository,
  analysisRepository: AnalysisRepository,
  // ---
  nodeDetailsPageBuilder: NodeDetailsPageBuilder,
  nodeMapPageBuilder: NodeMapPageBuilder,
  nodeChangesPageBuilder: NodeChangesPageBuilder,
  routePageBuilder: RoutePageBuilder,
  networkDetailsPageBuilder: NetworkDetailsPageBuilder,
  networkMapPageBuilder: NetworkMapPageBuilder,
  networkFactsPageBuilder: NetworkFactsPageBuilder,
  networkNodesPageBuilder: NetworkNodesPageBuilder,
  networkRoutesPageBuilder: NetworkRoutesPageBuilder,
  subsetNetworksPageBuilder: SubsetNetworksPageBuilder,
  subsetFactsPageBuilder: SubsetFactsPageBuilder,
  subsetFactDetailsPageBuilder: SubsetFactDetailsPageBuilder,
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

  override def nodeDetails(user: Option[String], nodeId: Long): ApiResponse[NodeDetailsPage] = {
    api.execute(user, "node-details", s"$nodeId") {
      reply(nodeDetailsPageBuilder.build(user, nodeId))
    }
  }

  override def nodeMap(user: Option[String], nodeId: Long): ApiResponse[NodeMapPage] = {
    api.execute(user, "node-map", s"$nodeId") {
      reply(nodeMapPageBuilder.build(user, nodeId))
    }
  }

  override def nodeChanges(user: Option[String], nodeId: Long, parameters: ChangesParameters): ApiResponse[NodeChangesPage] = {
    api.execute(user, "node-changes", s"node=$nodeId, ${parameters.toDisplayString}") {
      reply(nodeChangesPageBuilder.build(user, nodeId, parameters))
    }
  }

  override def routeDetails(user: Option[String], routeId: Long): ApiResponse[RouteDetailsPage] = {
    api.execute(user, "route-details", s"$routeId") {
      reply(routePageBuilder.buildDetailsPage(user, routeId))
    }
  }

  override def routeMap(user: Option[String], routeId: Long): ApiResponse[RouteMapPage] = {
    api.execute(user, "route-map", s"$routeId") {
      reply(routePageBuilder.buildMapPage(user, routeId))
    }
  }

  override def routeChanges(user: Option[String], routeId: Long, parameters: ChangesParameters): ApiResponse[RouteChangesPage] = {
    api.execute(user, "route-changes", s"route=$routeId, ${parameters.toDisplayString}") {
      reply(routePageBuilder.buildChangesPage(user, routeId, parameters))
    }
  }

  override def networkDetails(user: Option[String], networkId: Long): ApiResponse[NetworkDetailsPage] = {
    api.execute(user, "network-details", s"$networkId") {
      reply(networkDetailsPageBuilder.build(networkId))
    }
  }

  override def networkMap(user: Option[String], networkId: Long): ApiResponse[NetworkMapPage] = {
    api.execute(user, "network-map", s"$networkId") {
      reply(networkMapPageBuilder.build(networkId))
    }
  }

  override def networkFacts(user: Option[String], networkId: Long): ApiResponse[NetworkFactsPage] = {
    api.execute(user, "network-facts", s"$networkId") {
      reply(networkFactsPageBuilder.build(networkId))
    }
  }

  override def networkNodes(user: Option[String], networkId: Long): ApiResponse[NetworkNodesPage] = {
    api.execute(user, "network-nodes", s"$networkId") {
      reply(networkNodesPageBuilder.build(networkId))
    }
  }

  override def networkRoutes(user: Option[String], networkId: Long): ApiResponse[NetworkRoutesPage] = {
    api.execute(user, "network-routes", s"$networkId") {
      reply(networkRoutesPageBuilder.build(networkId))
    }
  }

  override def networkChanges(user: Option[String], networkId: Long, parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    api.execute(user, "network-changes", s"networkId=$networkId, ${parameters.toDisplayString}") {
      reply(networkChangesPageBuilder.build(user, networkId, parameters))
    }
  }

  override def gpx(user: Option[String], networkId: Long): Option[GpxFile] = {
    api.execute(user, "gpx", s"$networkId") {
      networkRepository.gpx(networkId)
    }
  }

  override def subsetNetworks(user: Option[String], subset: Subset): ApiResponse[SubsetNetworksPage] = {
    api.execute(user, "subset-networks", s"${subset.string}") {
      reply(Some(subsetNetworksPageBuilder.build(subset)))
    }
  }

  override def subsetFacts(user: Option[String], subset: Subset): ApiResponse[SubsetFactsPage] = {
    api.execute(user, "subset-facts", s"${subset.string}") {
      reply(Some(subsetFactsPageBuilder.build(subset)))
    }
  }

  override def subsetFactDetails(user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    api.execute(user, "subset-fact-details", s"${subset.string}, ${fact.name}") {
      reply(Some(subsetFactDetailsPageBuilder.build(subset, fact)))
    }
  }

  override def subsetChanges(user: Option[String], subset: Subset, parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    api.execute(user, "subset-changes", s"subset=${subset.name}, ${parameters.toDisplayString}") {
      reply(subsetChangesPageBuilder.build(user, subset, parameters))
    }
  }

  override def subsetOrphanRoutes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    api.execute(user, "subset-orphan-routes", subset.string) {
      reply(Some(subsetOrphanRoutesPageBuilder.build(subset)))
    }
  }

  override def subsetOrphanNodes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    api.execute(user, "subset-orphan-nodes", subset.string) {
      reply(Some(subsetOrphanNodesPageBuilder.build(subset)))
    }
  }

  override def subsetMap(user: Option[String], subset: Subset): ApiResponse[SubsetMapPage] = {
    api.execute(user, "subset-map", subset.string) {
      reply(Some(subsetMapPageBuilder.build(subset)))
    }
  }

  override def overview(user: Option[String]): ApiResponse[Seq[StatisticValues]] = {
    api.execute(user, "overview", "") {
      reply(Some(overviewRepository.statisticValues()))
    }
  }

  override def changeSet(user: Option[String], changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage] = {
    val args = s"changeSetId=$changeSetId, replicationId=${replicationId.map(_.name)}"
    api.execute(user, "change-set", args) {
      reply(changeSetPageBuilder.build(user, changeSetId, replicationId))
    }
  }

  override def replication(user: Option[String], changeSetId: Long): ApiResponse[Long] = {
    val args = s"changeSetId=$changeSetId}"
    api.execute(user, "change-set", args) {
      reply(changeSetPageBuilder.build(Some("app"), changeSetId, None).map(_.summary.key.replicationNumber))
    }
  }

  override def changes(user: Option[String], parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    api.execute(user, "changes", parameters.toDisplayString) {
      reply(Some(changesPageBuilder.build(user, parameters)))
    }
  }

  override def locations(user: Option[String], networkType: NetworkType, country: Country): ApiResponse[LocationsPage] = {
    api.execute(user, "location", networkType.name) {
      reply(locationsPageBuilder.build(networkType, country))
    }
  }

  override def locationNodes(user: Option[String], key: LocationKey, parameters: LocationNodesParameters): ApiResponse[LocationNodesPage] = {
    val locationKey = s"${key.networkType.name}, ${key.country.domain}, ${key.name}, "
    val locationParameters = s"${parameters.locationNodesType.name}, ${parameters.itemsPerPage}, ${parameters.pageIndex}"
    api.execute(user, "location-nodes", locationKey + locationParameters) {
      reply(locationNodesPageBuilder.build(key, parameters))
    }
  }

  override def locationRoutes(user: Option[String], locationKey: LocationKey, parameters: LocationRoutesParameters): ApiResponse[LocationRoutesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(user, "location-routes", args) {
      reply(locationRoutesPageBuilder.build(locationKey, parameters))
    }
  }

  override def locationFacts(user: Option[String], locationKey: LocationKey): ApiResponse[LocationFactsPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(user, "location-facts", args) {
      reply(locationFactsPageBuilder.build(locationKey))
    }
  }

  override def locationMap(user: Option[String], locationKey: LocationKey): ApiResponse[LocationMapPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(user, "location-map", args) {
      reply(locationMapPageBuilder.build(locationKey))
    }
  }

  override def locationChanges(user: Option[String], locationKey: LocationKey, parameters: LocationChangesParameters): ApiResponse[LocationChangesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(user, "location-changes", args) {
      reply(locationChangesPageBuilder.build(locationKey, parameters))
    }
  }

  override def locationEdit(user: Option[String], locationKey: LocationKey): ApiResponse[LocationEditPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    api.execute(user, "location-edit", args) {
      reply(locationEditPageBuilder.build(locationKey))
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(analysisRepository.lastUpdated(), 1, result)
    TimestampLocal.localize(response)
    response
  }
}
