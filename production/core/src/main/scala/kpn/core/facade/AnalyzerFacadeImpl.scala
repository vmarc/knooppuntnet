package kpn.core.facade

import kpn.core.app._
import kpn.core.app.stats.StatisticsBuilder
import kpn.core.db.couch.Couch
import kpn.core.facade.pages.ChangeSetPageBuilder
import kpn.core.facade.pages.ChangesPageBuilder
import kpn.core.facade.pages.NetworkChangesPageBuilder
import kpn.core.facade.pages.NetworkDetailsPageBuilder
import kpn.core.facade.pages.NetworkFactsPageBuilder
import kpn.core.facade.pages.NetworkMapPageBuilder
import kpn.core.facade.pages.NetworkNodesPageBuilder
import kpn.core.facade.pages.NetworkRoutesPageBuilder
import kpn.core.facade.pages.NodePageBuilder
import kpn.core.facade.pages.RoutePageBuilder
import kpn.core.facade.pages.SubsetChangesPageBuilder
import kpn.core.facade.pages.SubsetFactDetailsPageBuilder
import kpn.core.facade.pages.SubsetFactsPageBuilder
import kpn.core.facade.pages.SubsetNetworksPageBuilder
import kpn.core.facade.pages.SubsetOrphanNodesPageBuilder
import kpn.core.facade.pages.SubsetOrphanRoutesPageBuilder
import kpn.core.gpx.GpxFile
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.FactRepository
import kpn.core.repository.NetworkRepository
import kpn.core.repository.NodeRepository
import kpn.core.repository.OverviewRepository
import kpn.core.repository.RouteRepository
import kpn.core.util.Log
import kpn.shared.ApiResponse
import kpn.shared.ChangesPage
import kpn.shared.Fact
import kpn.shared.NetworkType
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
import kpn.shared.node.MapDetailNode
import kpn.shared.node.NodePage
import kpn.shared.route.MapDetailRoute
import kpn.shared.route.RoutePage
import kpn.shared.statistics.Statistics
import kpn.shared.subset.SubsetFactDetailsPage
import kpn.shared.subset.SubsetChangesPage
import kpn.shared.subset.SubsetFactsPage
import kpn.shared.subset.SubsetNetworksPage
import kpn.shared.subset.SubsetOrphanNodesPage
import kpn.shared.subset.SubsetOrphanRoutesPage

class AnalyzerFacadeImpl(
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
  networkChangesPageBuilder: NetworkChangesPageBuilder
) extends AnalyzerFacade {

  private val log = Log(classOf[AnalyzerFacadeImpl])

  override def node(user: Option[String], nodeId: Long): ApiResponse[NodePage] = {
    log.infoElapsed(s"$user node($nodeId)") {
      reply(nodePageBuilder.build(user, nodeId))
    }
  }

  override def route(user: Option[String], routeId: Long): ApiResponse[RoutePage] = {
    log.infoElapsed(s"$user route($routeId)") {
      reply(routePageBuilder.build(user, routeId))
    }
  }

  override def networkDetails(user: Option[String], networkId: Long): ApiResponse[NetworkDetailsPage] = {
    log.infoElapsed(s"$user networkDetails($networkId)") {
      reply(networkDetailsPageBuilder.build(networkId))
    }
  }

  override def networkMap(user: Option[String], networkId: Long): ApiResponse[NetworkMapPage] = {
    log.infoElapsed(s"$user networkMap($networkId)") {
      reply(networkMapPageBuilder.build(networkId))
    }
  }

  override def networkFacts(user: Option[String], networkId: Long): ApiResponse[NetworkFactsPage] = {
    log.infoElapsed(s"$user networkFacts($networkId)") {
      reply(networkFactsPageBuilder.build(networkId))
    }
  }

  override def networkNodes(user: Option[String], networkId: Long): ApiResponse[NetworkNodesPage] = {
    log.infoElapsed(s"$user networkNodes($networkId)") {
      reply(networkNodesPageBuilder.build(networkId))
    }
  }

  override def networkRoutes(user: Option[String], networkId: Long): ApiResponse[NetworkRoutesPage] = {
    log.infoElapsed(s"$user networkRoutes($networkId)") {
      reply(networkRoutesPageBuilder.build(networkId))
    }
  }

  override def networkChanges(user: Option[String], parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    log.infoElapsed(s"$user networkChanges(${parameters.toDisplayString})") {
      reply(networkChangesPageBuilder.build(user, parameters))
    }
  }

  override def gpx(user: Option[String], networkId: Long): Option[GpxFile] = {
    log.infoElapsed(s"$user gpx($networkId)") {
      networkRepository.gpx(networkId, Couch.uiTimeout)
    }
  }

  override def subsetNetworks(user: Option[String], subset: Subset): ApiResponse[SubsetNetworksPage] = {
    log.infoElapsed(s"$user subsetNetworks(${subset.string})") {
      reply(Some(subsetNetworksPageBuilder.build(subset)))
    }
  }

  override def subsetFacts(user: Option[String], subset: Subset): ApiResponse[SubsetFactsPage] = {
    log.infoElapsed(s"$user subsetFacts(${subset.string})") {
      reply(Some(subsetFactsPageBuilder.build(subset)))
    }
  }

  override def subsetFactDetails(user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    log.infoElapsed(s"$user facts(${subset.string})") {
      reply(Some(subsetFactDetailsPageBuilder.build(subset, fact)))
    }
  }

  override def subsetChanges(user: Option[String], parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    log.infoElapsed(s"$user subsetChanges(${parameters.toDisplayString})") {
      reply(subsetChangesPageBuilder.build(user, parameters))
    }
  }

  override def subsetOrphanRoutes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    log.infoElapsed(s"$user subsetOrphanRoutes(${subset.string})") {
      reply(Some(subsetOrphanRoutesPageBuilder.build(subset)))
    }
  }

  override def subsetOrphanNodes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    log.infoElapsed(s"$user subsetOrphanNodes(${subset.string})") {
      reply(Some(subsetOrphanNodesPageBuilder.build(subset)))
    }
  }

  override def overview(user: Option[String]): ApiResponse[Statistics] = {
    log.infoElapsed(s"$user overview()") {
      // TODO move into separate StaticsPageBuilder, and return StatisticsPage instead?
      val figures = overviewRepository.figures(Couch.uiTimeout)
      val page = StatisticsBuilder.build(figures)
      reply(Some(page))
    }
  }

  override def integrityCheckFacts(user: Option[String], country: String, networkType: String): IntegrityCheckPage = {
    log.infoElapsed(s"$user integrityCheckFacts($country/$networkType)") {
      factRepository.integrityCheckFacts(country, networkType, Couch.uiTimeout)
    }
  }

  override def changeSet(user: Option[String], changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage] = {
    log.infoElapsed(s"$user changeSet(changeSetId=$changeSetId, replicationId=${replicationId.map(_.name)})") {
      reply(changeSetPageBuilder.build(user, changeSetId, replicationId))
    }
  }

  override def changes(user: Option[String], parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    log.infoElapsed(s"$user changes(${parameters.toDisplayString})") {
      reply(Some(changesPageBuilder.build(user, parameters)))
    }
  }

  override def mapDetailNode(user: Option[String], networkType: NetworkType, nodeId: Long): ApiResponse[MapDetailNode] = {
    log.infoElapsed(s"$user mapDetailNode(${networkType.name}, $nodeId)") {
      val page = nodeRepository.nodeWithId(nodeId, Couch.uiTimeout).map { nodeInfo =>
        val nodeReferences = nodeRepository.networkTypeNodeReferences(networkType, nodeId, Couch.uiTimeout)
        MapDetailNode(nodeInfo, nodeReferences)
      }
      ApiResponse(None, 1, page) // analysis timestamp not needed here
    }
  }

  override def mapDetailRoute(user: Option[String], routeId: Long): ApiResponse[MapDetailRoute] = {
    log.infoElapsed(s"$user mapDetailRoute($routeId)") {
      val page = routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
        val routeWithoutAnalysis = route.copy(analysis = None)
        val routeReferences = routeRepository.routeReferences(routeId, Couch.uiTimeout)
        MapDetailRoute(routeWithoutAnalysis, routeReferences)
      }
      ApiResponse(None, 1, page) // analysis timestamp not needed here
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    ApiResponse(analysisRepository.lastUpdated(), 1, result)
  }

}
