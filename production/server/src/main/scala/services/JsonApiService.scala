package services

import akka.actor.ActorSystem
import kpn.core.facade.AnalyzerFacade
import kpn.shared.ApiResponse
import kpn.shared.ChangesPage
import kpn.shared.Fact
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
import kpn.shared.node.MapDetailNode
import kpn.shared.node.NodePage
import kpn.shared.planner.RouteLeg
import kpn.shared.route.MapDetailRoute
import kpn.shared.route.RoutePage
import kpn.shared.statistics.Statistics
import kpn.shared.subset.SubsetChangesPage
import kpn.shared.subset.SubsetFactDetailsPage
import kpn.shared.subset.SubsetFactsPageNew
import kpn.shared.subset.SubsetNetworksPage
import kpn.shared.subset.SubsetOrphanNodesPage
import kpn.shared.subset.SubsetOrphanRoutesPage
import kpn.shared.tiles.ClientPoiConfiguration

class JsonApiService(analyzerFacade: AnalyzerFacade, user: Option[String] = None)(implicit val system: ActorSystem) {

  def overview(): ApiResponse[Statistics] = {
    analyzerFacade.overview(user)
  }

  def subsetNetworks(subset: Subset): ApiResponse[SubsetNetworksPage] = {
    analyzerFacade.subsetNetworks(user, subset)
  }

  def subsetFacts(subset: Subset): ApiResponse[SubsetFactsPageNew] = {
    analyzerFacade.subsetFactsNew(user, subset)
  }

  def subsetFactDetails(subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    analyzerFacade.subsetFactDetails(user, subset, fact)
  }

  def subsetOrphanNodes(subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    analyzerFacade.subsetOrphanNodes(user, subset)
  }

  def subsetOrphanRoutes(subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    analyzerFacade.subsetOrphanRoutes(user, subset)
  }

  def subsetChanges(parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    analyzerFacade.subsetChanges(user, parameters)
  }

  def networkDetails(networkId: Long): ApiResponse[NetworkDetailsPage] = {
    analyzerFacade.networkDetails(user, networkId)
  }

  def networkMap(networkId: Long): ApiResponse[NetworkMapPage] = {
    analyzerFacade.networkMap(user, networkId)
  }

  def networkFacts(networkId: Long): ApiResponse[NetworkFactsPage] = {
    analyzerFacade.networkFacts(user, networkId)
  }

  def networkNodes(networkId: Long): ApiResponse[NetworkNodesPage] = {
    analyzerFacade.networkNodes(user, networkId)
  }

  def networkRoutes(networkId: Long): ApiResponse[NetworkRoutesPage] = {
    analyzerFacade.networkRoutes(user, networkId)
  }

  def networkChanges(parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    analyzerFacade.networkChanges(user, parameters)
  }

  def node(nodeId: Long): ApiResponse[NodePage] = {
    analyzerFacade.node(user, nodeId)
  }

  def route(routeId: Long): ApiResponse[RoutePage] = {
    analyzerFacade.route(user, routeId)
  }

  def changes(parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    analyzerFacade.changes(user, parameters)
  }

  def changeSet(changeSetId: Long, replicationNumber: Int): ApiResponse[ChangeSetPage] = {
    val replicationId = ReplicationId(replicationNumber)
    analyzerFacade.changeSet(user, changeSetId, Some(replicationId))
  }

  def mapDetailNode(networkType: NetworkType, nodeId: Long): ApiResponse[MapDetailNode] = {
    analyzerFacade.mapDetailNode(user, networkType, nodeId)
  }

  def mapDetailRoute(routeId: Long): ApiResponse[MapDetailRoute] = {
    analyzerFacade.mapDetailRoute(user, routeId)
  }

  def poiConfiguration(): ApiResponse[ClientPoiConfiguration] = {
    analyzerFacade.poiConfiguration(user)
  }

  def poi(elementType: String, elementId: Long): ApiResponse[PoiPage] = {
    analyzerFacade.poi(user, elementType, elementId)
  }

  def leg(networkType: NetworkType, legId: String, sourceNodeId: String, sinkNodeId: String): ApiResponse[RouteLeg] = {
    analyzerFacade.leg(user, networkType, legId, sourceNodeId, sinkNodeId)
  }

}
