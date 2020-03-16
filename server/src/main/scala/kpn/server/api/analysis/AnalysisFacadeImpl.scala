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
import kpn.api.common.location.LocationPage
import kpn.api.common.location.LocationRoutesPage
import kpn.api.common.location.LocationRoutesParameters
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
import kpn.api.common.subset.SubsetNetworksPage
import kpn.api.common.subset.SubsetOrphanNodesPage
import kpn.api.common.subset.SubsetOrphanRoutesPage
import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.api.custom.ApiResponse
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
import kpn.server.api.analysis.pages.ChangeSetPageBuilder
import kpn.server.api.analysis.pages.ChangesPageBuilder
import kpn.server.api.analysis.pages.LegBuilder
import kpn.server.api.analysis.pages.LocationPageBuilder
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
  changesPageBuilder: ChangesPageBuilder,
  changeSetPageBuilder: ChangeSetPageBuilder,
  networkChangesPageBuilder: NetworkChangesPageBuilder,
  poiPageBuilder: PoiPageBuilder,
  legBuilder: LegBuilder,
  mapNodeDetailBuilder: MapNodeDetailBuilder,
  mapRouteDetailBuilder: MapRouteDetailBuilder,
  locationPageBuilder: LocationPageBuilder,
  locationNodesPageBuilder: LocationNodesPageBuilder,
  locationRoutesPageBuilder: LocationRoutesPageBuilder,
  locationFactsPageBuilder: LocationFactsPageBuilder,
  locationMapPageBuilder: LocationMapPageBuilder,
  locationChangesPageBuilder: LocationChangesPageBuilder
) extends AnalysisFacade {

  private val log = Log(classOf[AnalysisFacadeImpl])

  override def nodeDetails(user: Option[String], nodeId: Long): ApiResponse[NodeDetailsPage] = {
    val label = s"$user node($nodeId)"
    log.infoElapsed(label) {
      reply(label, nodePageBuilder.buildDetailsPage(user, nodeId))
    }
  }

  override def nodeMap(user: Option[String], nodeId: Long): ApiResponse[NodeMapPage] = {
    val label = s"$user node($nodeId)"
    log.infoElapsed(label) {
      reply(label, nodePageBuilder.buildMapPage(user, nodeId))
    }
  }

  override def nodeChanges(user: Option[String], nodeId: Long, parameters: ChangesParameters): ApiResponse[NodeChangesPage] = {
    val label = s"$user node($nodeId)"
    log.infoElapsed(label) {
      reply(label, nodePageBuilder.buildChangesPage(user, nodeId, parameters))
    }
  }

  override def routeDetails(user: Option[String], routeId: Long): ApiResponse[RouteDetailsPage] = {
    val label = s"$user route($routeId)"
    log.infoElapsed(label) {
      reply(label, routePageBuilder.buildDetailsPage(user, routeId))
    }
  }

  override def routeMap(user: Option[String], routeId: Long): ApiResponse[RouteMapPage] = {
    val label = s"$user route($routeId)"
    log.infoElapsed(label) {
      reply(label, routePageBuilder.buildMapPage(user, routeId))
    }
  }

  override def routeChanges(user: Option[String], routeId: Long, parameters: ChangesParameters): ApiResponse[RouteChangesPage] = {
    val label = s"$user route($routeId)"
    log.infoElapsed(label) {
      reply(label, routePageBuilder.buildChangesPage(user, routeId, parameters))
    }
  }

  override def networkDetails(user: Option[String], networkId: Long): ApiResponse[NetworkDetailsPage] = {
    val label = s"$user networkDetails($networkId)"
    log.infoElapsed(label) {
      reply(label, networkDetailsPageBuilder.build(networkId))
    }
  }

  override def networkMap(user: Option[String], networkId: Long): ApiResponse[NetworkMapPage] = {
    val label = s"$user networkMap($networkId)"
    log.infoElapsed(label) {
      reply(label, networkMapPageBuilder.build(networkId))
    }
  }

  override def networkFacts(user: Option[String], networkId: Long): ApiResponse[NetworkFactsPage] = {
    val label = s"$user networkFacts($networkId)"
    log.infoElapsed(label) {
      reply(label, networkFactsPageBuilder.build(networkId))
    }
  }

  override def networkNodes(user: Option[String], networkId: Long): ApiResponse[NetworkNodesPage] = {
    val label = s"$user networkNodes($networkId)"
    log.infoElapsed(label) {
      reply(label, networkNodesPageBuilder.build(networkId))
    }
  }

  override def networkRoutes(user: Option[String], networkId: Long): ApiResponse[NetworkRoutesPage] = {
    val label = s"$user networkRoutes($networkId)"
    log.infoElapsed(label) {
      reply(label, networkRoutesPageBuilder.build(networkId))
    }
  }

  override def networkChanges(user: Option[String], parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    val label = s"$user networkChanges(${parameters.toDisplayString})"
    log.infoElapsed(label) {
      reply(label, networkChangesPageBuilder.build(user, parameters))
    }
  }

  override def gpx(user: Option[String], networkId: Long): Option[GpxFile] = {
    val label = s"$user gpx($networkId)"
    log.infoElapsed(label) {
      networkRepository.gpx(networkId, Couch.uiTimeout)
    }
  }

  override def subsetNetworks(user: Option[String], subset: Subset): ApiResponse[SubsetNetworksPage] = {
    val label = s"$user subsetNetworks(${subset.string})"
    log.infoElapsed(label) {
      reply(label, Some(subsetNetworksPageBuilder.build(subset)))
    }
  }

  override def subsetFacts(user: Option[String], subset: Subset): ApiResponse[SubsetFactsPage] = {
    val label = s"$user subsetFacts(${subset.string})"
    log.infoElapsed(label) {
      reply(label, Some(subsetFactsPageBuilder.build(subset)))
    }
  }

  override def subsetFactDetails(user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    val label = s"$user facts(${subset.string})"
    log.infoElapsed(label) {
      reply(label, Some(subsetFactDetailsPageBuilder.build(subset, fact)))
    }
  }

  override def subsetChanges(user: Option[String], parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    val label = s"$user subsetChanges(${parameters.toDisplayString})"
    log.infoElapsed(label) {
      reply(label, subsetChangesPageBuilder.build(user, parameters))
    }
  }

  override def subsetOrphanRoutes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    val label = s"$user subsetOrphanRoutes(${subset.string})"
    log.infoElapsed(label) {
      reply(label, Some(subsetOrphanRoutesPageBuilder.build(subset)))
    }
  }

  override def subsetOrphanNodes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    val label = s"$user subsetOrphanNodes(${subset.string})"
    log.infoElapsed(label) {
      reply(label, Some(subsetOrphanNodesPageBuilder.build(subset)))
    }
  }

  override def overview(user: Option[String]): ApiResponse[Statistics] = {
    val label = s"$user overview()"
    log.infoElapsed(label) {
      // TODO move into separate StaticsPageBuilder, and return StatisticsPage instead?
      val figures = overviewRepository.figures(Couch.uiTimeout)
      val page = StatisticsBuilder.build(figures)
      reply(label, Some(page))
    }
  }

  override def changeSet(user: Option[String], changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage] = {
    val label = s"$user changeSet(changeSetId=$changeSetId, replicationId=${replicationId.map(_.name)})"
    log.infoElapsed(label) {
      reply(label, changeSetPageBuilder.build(user, changeSetId, replicationId))
    }
  }

  override def changes(user: Option[String], parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    val label = s"$user changes(${parameters.toDisplayString})"
    log.infoElapsed(label) {
      reply(label, Some(changesPageBuilder.build(user, parameters)))
    }
  }

  override def mapNodeDetail(user: Option[String], networkType: NetworkType, nodeId: Long): ApiResponse[MapNodeDetail] = {
    val label = s"$user mapNodeDetail(${networkType.name}, $nodeId)"
    log.infoElapsed(label) {
      reply(label, mapNodeDetailBuilder.build(user, networkType, nodeId))
    }
  }

  override def mapRouteDetail(user: Option[String], routeId: Long): ApiResponse[MapRouteDetail] = {
    val label = s"$user mapRouteDetail($routeId)"
    log.infoElapsed(label) {
      reply(label, mapRouteDetailBuilder.build(user, routeId))
    }
  }

  override def poiConfiguration(user: Option[String]): ApiResponse[ClientPoiConfiguration] = {
    log.infoElapsed(s"$user poiConfiguration") {
      ApiResponse(None, 1, Some(PoiConfiguration.instance.toClient))
    }
  }

  def poi(user: Option[String], poiRef: PoiRef): ApiResponse[PoiPage] = {
    log.infoElapsed(s"$user poi(${poiRef.elementType}, ${poiRef.elementId})") {
      val poiPage = poiPageBuilder.build(poiRef)
      ApiResponse(None, 1, poiPage) // analysis timestamp not needed here
    }
  }

  override def location(user: Option[String], networkType: NetworkType): ApiResponse[LocationPage] = {
    val label = s"$user location(${networkType.name})"
    log.infoElapsed(label) {
      reply(label, locationPageBuilder.build(networkType))
    }
  }

  def locationNodes(user: Option[String], locationKey: LocationKey, parameters: LocationNodesParameters): ApiResponse[LocationNodesPage] = {
    val label = s"$user locationNodes(${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name})"
    log.infoElapsed(label) {
      reply(label, locationNodesPageBuilder.build(locationKey, parameters))
    }
  }

  def locationRoutes(user: Option[String], locationKey: LocationKey, parameters: LocationRoutesParameters): ApiResponse[LocationRoutesPage] = {
    val label = s"$user locationRoutes(${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name})"
    log.infoElapsed(label) {
      reply(label, locationRoutesPageBuilder.build(locationKey, parameters))
    }
  }

  def locationFacts(user: Option[String], locationKey: LocationKey): ApiResponse[LocationFactsPage] = {
    val label = s"$user locationFacts(${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name})"
    log.infoElapsed(label) {
      reply(label, locationFactsPageBuilder.build(locationKey))
    }
  }

  def locationMap(user: Option[String], locationKey: LocationKey): ApiResponse[LocationMapPage] = {
    val label = s"$user locationMap(${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name})"
    log.infoElapsed(label) {
      reply(label, locationMapPageBuilder.build(locationKey))
    }
  }

  def locationChanges(user: Option[String], locationKey: LocationKey, parameters: LocationChangesParameters): ApiResponse[LocationChangesPage] = {
    val label = s"$user locationChanges(${locationKey.networkType.name}, ${locationKey.country.domain}, ${locationKey.name})"
    log.infoElapsed(label) {
      reply(label, locationChangesPageBuilder.build(locationKey, parameters))
    }
  }

  override def leg(user: Option[String], networkType: NetworkType, legId: String, sourceNodeId: String, sinkNodeId: String): ApiResponse[RouteLeg] = {
    log.infoElapsed(s"$user leg($legId, $sourceNodeId, $sinkNodeId)") {
      val leg = legBuilder.build(networkType, legId, sourceNodeId, sinkNodeId)
      ApiResponse(None, 1, leg)
    }
  }

  private def reply[T](label: String, result: Option[T]): ApiResponse[T] = {
    log.infoElapsed(s"timestamp localize " + label) {
      val r = ApiResponse(analysisRepository.lastUpdated(), 1, result)
      TimestampLocal.localize(r)
      r
    }
  }

}
