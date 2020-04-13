package kpn.server.api.analysis

import kpn.api.common.ChangesPage
import kpn.api.common.PoiPage
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetPage
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.location.LocationChangesPage
import kpn.api.common.location.LocationChangesParameters
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
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.node.NodeChangesPage
import kpn.api.common.node.NodeDetailsPage
import kpn.api.common.node.NodeMapPage
import kpn.api.common.planner.RouteLeg
import kpn.api.common.route.MapRouteDetail
import kpn.api.common.route.RouteChangesPage
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteMapPage
import kpn.api.common.subset.SubsetChangesPage
import kpn.api.common.subset.SubsetFactDetailsPage
import kpn.api.common.subset.SubsetFactsPage
import kpn.api.common.subset.SubsetMapPage
import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.api.custom.ApiResponse
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.api.custom.Statistics
import kpn.api.custom.Subset
import kpn.core.app.stats.StatisticsBuilder
import kpn.core.common.TimestampLocal
import kpn.core.db.couch.Couch
import kpn.core.gpx.GpxFile
import kpn.core.poi.PoiConfiguration
import kpn.core.util.Log
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.Api
import kpn.server.api.analysis.pages.ChangeSetPageBuilder
import kpn.server.api.analysis.pages.ChangesPageBuilder
import kpn.server.api.analysis.pages.LegBuilder
import kpn.server.api.analysis.pages.LocationsPageBuilder
import kpn.server.api.analysis.pages.PoiPageBuilder
import kpn.server.api.analysis.pages.location.LocationChangesPageBuilder
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
import kpn.server.api.analysis.pages.node.MapNodeDetailBuilder
import kpn.server.api.analysis.pages.node.NodePageBuilder
import kpn.server.api.analysis.pages.route.MapRouteDetailBuilder
import kpn.server.api.analysis.pages.route.RoutePageBuilder
import kpn.server.api.analysis.pages.subset.SubsetChangesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactDetailsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetMapPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetNetworksPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanNodesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanRoutesPageBuilder
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.FactRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.OverviewRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class AnalysisFacadeImpl(
  api: Api,
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository,
  networkRepository: NetworkRepository,
  overviewRepository: OverviewRepository,
  factRepository: FactRepository,
  analysisRepository: AnalysisRepository,
  // ---
  nodePageBuilder: NodePageBuilder,
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
  poiPageBuilder: PoiPageBuilder,
  legBuilder: LegBuilder,
  mapNodeDetailBuilder: MapNodeDetailBuilder,
  mapRouteDetailBuilder: MapRouteDetailBuilder,
  locationsPageBuilder: LocationsPageBuilder,
  locationNodesPageBuilder: LocationNodesPageBuilder,
  locationRoutesPageBuilder: LocationRoutesPageBuilder,
  locationFactsPageBuilder: LocationFactsPageBuilder,
  locationMapPageBuilder: LocationMapPageBuilder,
  locationChangesPageBuilder: LocationChangesPageBuilder
) extends AnalysisFacade {

  private val log = Log(classOf[AnalysisFacadeImpl])

  override def nodeDetails(user: Option[String], nodeId: Long): ApiResponse[NodeDetailsPage] = {
    execute(user, "node-details", s"$nodeId") {
      nodePageBuilder.buildDetailsPage(user, nodeId)
    }
  }

  override def nodeMap(user: Option[String], nodeId: Long): ApiResponse[NodeMapPage] = {
    execute(user, "node-map", s"$nodeId") {
      nodePageBuilder.buildMapPage(user, nodeId)
    }
  }

  override def nodeChanges(user: Option[String], nodeId: Long, parameters: ChangesParameters): ApiResponse[NodeChangesPage] = {
    execute(user, "node-changes", s"$nodeId") { // TODO add parameters in args?
      nodePageBuilder.buildChangesPage(user, nodeId, parameters)
    }
  }

  override def routeDetails(user: Option[String], routeId: Long): ApiResponse[RouteDetailsPage] = {
    execute(user, "route-details", s"$routeId") {
      routePageBuilder.buildDetailsPage(user, routeId)
    }
  }

  override def routeMap(user: Option[String], routeId: Long): ApiResponse[RouteMapPage] = {
    execute(user, "route-map", s"$routeId") {
      routePageBuilder.buildMapPage(user, routeId)
    }
  }

  override def routeChanges(user: Option[String], routeId: Long, parameters: ChangesParameters): ApiResponse[RouteChangesPage] = {
    execute(user, "route-changes", s"$routeId") {
      routePageBuilder.buildChangesPage(user, routeId, parameters)
    }
  }

  override def networkDetails(user: Option[String], networkId: Long): ApiResponse[NetworkDetailsPage] = {
    execute(user, "network-details", s"$networkId") {
      networkDetailsPageBuilder.build(networkId)
    }
  }

  override def networkMap(user: Option[String], networkId: Long): ApiResponse[NetworkMapPage] = {
    execute(user, "network-map", s"$networkId") {
      networkMapPageBuilder.build(networkId)
    }
  }

  override def networkFacts(user: Option[String], networkId: Long): ApiResponse[NetworkFactsPage] = {
    execute(user, "network-facts", s"$networkId") {
      networkFactsPageBuilder.build(networkId)
    }
  }

  override def networkNodes(user: Option[String], networkId: Long): ApiResponse[NetworkNodesPage] = {
    execute(user, "network-nodes", s"$networkId") {
      networkNodesPageBuilder.build(networkId)
    }
  }

  override def networkRoutes(user: Option[String], networkId: Long): ApiResponse[NetworkRoutesPage] = {
    execute(user, "network-routes", s"$networkId") {
      networkRoutesPageBuilder.build(networkId)
    }
  }

  override def networkChanges(user: Option[String], parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    execute(user, "network-changes", s"${parameters.toDisplayString}") {
      networkChangesPageBuilder.build(user, parameters)
    }
  }

  override def gpx(user: Option[String], networkId: Long): Option[GpxFile] = {
    api.execute(user, "gpx", s"$networkId") {
      networkRepository.gpx(networkId, Couch.uiTimeout)
    }
  }

  override def subsetNetworks(user: Option[String], subset: Subset): ApiResponse[SubsetNetworksPage] = {
    execute(user, "subset-networks", s"${subset.string}") {
      Some(subsetNetworksPageBuilder.build(subset))
    }
  }

  override def subsetFacts(user: Option[String], subset: Subset): ApiResponse[SubsetFactsPage] = {
    execute(user, "subset-facts", s"${subset.string}") {
      Some(subsetFactsPageBuilder.build(subset))
    }
  }

  override def subsetFactDetails(user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    execute(user, "subset-fact-details", s"${subset.string}, ${fact.name}") {
      Some(subsetFactDetailsPageBuilder.build(subset, fact))
    }
  }

  override def subsetChanges(user: Option[String], parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    execute(user, "subset-changes", parameters.toDisplayString) {
      subsetChangesPageBuilder.build(user, parameters)
    }
  }

  override def subsetOrphanRoutes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    execute(user, "subset-orphan-routes", subset.string) {
      Some(subsetOrphanRoutesPageBuilder.build(subset))
    }
  }

  override def subsetOrphanNodes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    execute(user, "subset-orphan-nodes", subset.string) {
      Some(subsetOrphanNodesPageBuilder.build(subset))
    }
  }

  override def subsetMap(user: Option[String], subset: Subset): ApiResponse[SubsetMapPage] = {
    execute(user, "subset-map", subset.string) {
      Some(subsetMapPageBuilder.build(subset))
    }
  }

  override def overview(user: Option[String]): ApiResponse[Statistics] = {
    api.execute(user, "overview", "") {
      // TODO move into separate StaticsPageBuilder, and return StatisticsPage instead?
      val figures = overviewRepository.figures(Couch.uiTimeout)
      val page = StatisticsBuilder.build(figures)
      reply(Some(page))
    }
  }

  override def changeSet(user: Option[String], changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage] = {
    val args = s"changeSetId=$changeSetId, replicationId=${replicationId.map(_.name)}"
    execute(user, "change-set", args) {
      changeSetPageBuilder.build(user, changeSetId, replicationId)
    }
  }

  override def changes(user: Option[String], parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    execute(user, "changes", parameters.toDisplayString) {
      Some(changesPageBuilder.build(user, parameters))
    }
  }

  override def mapNodeDetail(user: Option[String], networkType: NetworkType, nodeId: Long): ApiResponse[MapNodeDetail] = {
    val args = s"${networkType.name}, $nodeId"
    execute(user, "map-node-detail", args) {
      mapNodeDetailBuilder.build(user, networkType, nodeId)
    }
  }

  override def mapRouteDetail(user: Option[String], routeId: Long): ApiResponse[MapRouteDetail] = {
    execute(user, "map-route-detail", routeId.toString) {
      mapRouteDetailBuilder.build(user, routeId)
    }
  }

  override def poiConfiguration(user: Option[String]): ApiResponse[ClientPoiConfiguration] = {
    api.execute(user, "poiConfiguration", "") {
      ApiResponse(None, 1, Some(PoiConfiguration.instance.toClient))
    }
  }

  def poi(user: Option[String], poiRef: PoiRef): ApiResponse[PoiPage] = {
    api.execute(user, "poi", s"${poiRef.elementType}, ${poiRef.elementId}") {
      val poiPage = poiPageBuilder.build(poiRef)
      ApiResponse(None, 1, poiPage) // analysis timestamp not needed here
    }
  }

  override def locations(user: Option[String], networkType: NetworkType, country: Country): ApiResponse[LocationsPage] = {
    execute(user, "location", networkType.name) {
      locationsPageBuilder.build(networkType, country)
    }
  }

  def locationNodes(user: Option[String], locationKey: LocationKey, parameters: LocationNodesParameters): ApiResponse[LocationNodesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    execute(user, "location-nodes", args) {
      locationNodesPageBuilder.build(locationKey, parameters)
    }
  }

  def locationRoutes(user: Option[String], locationKey: LocationKey, parameters: LocationRoutesParameters): ApiResponse[LocationRoutesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    execute(user, "location-routes", args) {
      locationRoutesPageBuilder.build(locationKey, parameters)
    }
  }

  def locationFacts(user: Option[String], locationKey: LocationKey): ApiResponse[LocationFactsPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    execute(user, "location-facts", args) {
      locationFactsPageBuilder.build(locationKey)
    }
  }

  def locationMap(user: Option[String], locationKey: LocationKey): ApiResponse[LocationMapPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    execute(user, "location-map", args) {
      locationMapPageBuilder.build(locationKey)
    }
  }

  def locationChanges(user: Option[String], locationKey: LocationKey, parameters: LocationChangesParameters): ApiResponse[LocationChangesPage] = {
    val args = s"${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name}"
    execute(user, "location-changes", args) {
      locationChangesPageBuilder.build(locationKey, parameters)
    }
  }

  override def leg(user: Option[String], networkType: NetworkType, legId: String, sourceNodeId: String, sinkNodeId: String): ApiResponse[RouteLeg] = {
    api.execute(user, "leg", s"$legId, $sourceNodeId, $sinkNodeId") {
      val leg = legBuilder.build(networkType, legId, sourceNodeId, sinkNodeId)
      ApiResponse(None, 1, leg)
    }
  }

  private def execute[T](user: Option[String], action: String, args: String)(result: Option[T]): ApiResponse[T] = {
    api.execute(user, action, args) {
      reply(result)
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(analysisRepository.lastUpdated(), 1, result)
    TimestampLocal.localize(response)
    response
  }

}
