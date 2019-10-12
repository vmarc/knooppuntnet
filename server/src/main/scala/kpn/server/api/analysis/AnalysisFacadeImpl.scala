package kpn.server.api.analysis

import kpn.core.app._
import kpn.core.app.stats.StatisticsBuilder
import kpn.core.common.TimestampLocal
import kpn.core.db.couch.Couch
import kpn.core.gpx.GpxFile
import kpn.core.poi.PoiConfiguration
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.FactRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.OverviewRepository
import kpn.server.repository.RouteRepository
import kpn.core.util.Log
import kpn.server.api.analysis.pages.ChangeSetPageBuilder
import kpn.server.api.analysis.pages.ChangesPageBuilder
import kpn.server.api.analysis.pages.LegBuilder
import kpn.server.api.analysis.pages.LocationPageBuilder
import kpn.server.api.analysis.pages.PoiPageBuilder
import kpn.server.api.analysis.pages.network.NetworkChangesPageBuilder
import kpn.server.api.analysis.pages.network.NetworkDetailsPageBuilder
import kpn.server.api.analysis.pages.network.NetworkFactsPageBuilder
import kpn.server.api.analysis.pages.network.NetworkMapPageBuilder
import kpn.server.api.analysis.pages.network.NetworkNodesPageBuilder
import kpn.server.api.analysis.pages.network.NetworkRoutesPageBuilder
import kpn.server.api.analysis.pages.node.NodePageBuilder
import kpn.server.api.analysis.pages.route.RoutePageBuilder
import kpn.server.api.analysis.pages.subset.SubsetChangesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactDetailsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetFactsPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetNetworksPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanNodesPageBuilder
import kpn.server.api.analysis.pages.subset.SubsetOrphanRoutesPageBuilder
import kpn.shared.ApiResponse
import kpn.shared.ChangesPage
import kpn.shared.Fact
import kpn.shared.LocationPage
import kpn.shared.NetworkType
import kpn.shared.PoiPage
import kpn.shared.ReplicationId
import kpn.shared.Subset
import kpn.shared.changes.ChangeSetPage
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.network.NetworkChangesPage
import kpn.shared.network.NetworkDetailsPage
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkMapPage
import kpn.shared.network.NetworkNodesPage
import kpn.shared.network.NetworkRoutesPage
import kpn.shared.network.OldNetworkFactsPage
import kpn.shared.node.MapDetailNode
import kpn.shared.node.NodeChangesPage
import kpn.shared.node.NodeDetailsPage
import kpn.shared.node.NodeMapPage
import kpn.shared.node.NodePage
import kpn.shared.node.NodeReferences
import kpn.shared.planner.RouteLeg
import kpn.shared.route.MapDetailRoute
import kpn.shared.route.RouteChangesPage
import kpn.shared.route.RouteDetailsPage
import kpn.shared.route.RouteMapPage
import kpn.shared.route.RoutePage
import kpn.shared.statistics.Statistics
import kpn.shared.subset.SubsetChangesPage
import kpn.shared.subset.SubsetFactDetailsPage
import kpn.shared.subset.SubsetFactsPage
import kpn.shared.subset.SubsetFactsPageNew
import kpn.shared.subset.SubsetNetworksPage
import kpn.shared.subset.SubsetOrphanNodesPage
import kpn.shared.subset.SubsetOrphanRoutesPage
import kpn.shared.tiles.ClientPoiConfiguration
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
  locationPageBuilder: LocationPageBuilder
) extends AnalysisFacade {

  private val log = Log(classOf[AnalysisFacadeImpl])

  override def node(user: Option[String], nodeId: Long): ApiResponse[NodePage] = {
    val label = s"$user node($nodeId)"
    log.infoElapsed(label) {
      reply(label, nodePageBuilder.build(user, nodeId))
    }
  }

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

  override def route(user: Option[String], routeId: Long): ApiResponse[RoutePage] = {
    val label = s"$user route($routeId)"
    log.infoElapsed(label) {
      reply(label, routePageBuilder.build(user, routeId))
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

  override def oldNetworkFacts(user: Option[String], networkId: Long): ApiResponse[OldNetworkFactsPage] = {
    val label = s"$user networkFacts($networkId)"
    log.infoElapsed(label) {
      reply(label, networkFactsPageBuilder.oldBuild(networkId))
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

  override def subsetFactsNew(user: Option[String], subset: Subset): ApiResponse[SubsetFactsPageNew] = {
    val label = s"$user subsetFacts(${subset.string})"
    log.infoElapsed(label) {
      reply(label, Some(subsetFactsPageBuilder.buildNew(subset)))
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

  override def integrityCheckFacts(user: Option[String], country: String, networkType: String): IntegrityCheckPage = {
    val label = s"$user integrityCheckFacts($country/$networkType)"
    log.infoElapsed(label) {
      factRepository.integrityCheckFacts(country, networkType, Couch.uiTimeout)
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

  override def mapDetailNode(user: Option[String], networkType: NetworkType, nodeId: Long): ApiResponse[MapDetailNode] = {
    val label = s"$user mapDetailNode(${networkType.name}, $nodeId)"
    log.infoElapsed(s"$user mapDetailNode(${networkType.name}, $nodeId)") {
      val page = nodeRepository.nodeWithId(nodeId, Couch.uiTimeout).map { nodeInfo =>
        val nodeNetworkReferences = nodeRepository.nodeNetworkReferences(nodeInfo.id, Couch.uiTimeout)
        val nodeOrphanRouteReferences = nodeRepository.nodeOrphanRouteReferences(nodeInfo.id, Couch.uiTimeout)
        val nodeReferences = NodeReferences(
          nodeNetworkReferences.filter(_.networkType == networkType),
          nodeOrphanRouteReferences.filter(_.networkType == networkType)
        )
        MapDetailNode(nodeInfo, nodeReferences)
      }
      log.infoElapsed(s"timestamp localize " + label) {
        TimestampLocal.localize(page)
      }
      ApiResponse(None, 1, page) // analysis timestamp not needed here
    }
  }

  override def mapDetailRoute(user: Option[String], routeId: Long): ApiResponse[MapDetailRoute] = {
    val label = s"$user mapDetailRoute($routeId)"
    log.infoElapsed(s"$user mapDetailRoute($routeId)") {
      val page = routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
        val routeWithoutAnalysis = route.copy(analysis = None)
        val routeReferences = routeRepository.routeReferences(routeId, Couch.uiTimeout)
        MapDetailRoute(routeWithoutAnalysis, routeReferences)
      }
      log.infoElapsed(s"timestamp localize " + label) {
        TimestampLocal.localize(page)
      }
      ApiResponse(None, 1, page) // analysis timestamp not needed here
    }
  }

  override def poiConfiguration(user: Option[String]): ApiResponse[ClientPoiConfiguration] = {
    val label = s"$user poiConfiguration"
    ApiResponse(None, 1, Some(PoiConfiguration.instance.toClient))
  }

  def poi(user: Option[String], elementType: String, elementId: Long): ApiResponse[PoiPage] = {
    log.infoElapsed(s"$user poi($elementType, $elementId)") {
      val poiPage = poiPageBuilder.build(elementType, elementId)
      ApiResponse(None, 1, poiPage) // analysis timestamp not needed here
    }
  }

  override def location(user: Option[String], networkType: NetworkType): ApiResponse[LocationPage] = {
    val label = s"$user location(${networkType.newName})"
    log.infoElapsed(label) {
      reply(label, locationPageBuilder.build(networkType))
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
