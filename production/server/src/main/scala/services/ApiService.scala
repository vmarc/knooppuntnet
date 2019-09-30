package services

import akka.actor.ActorSystem
import kpn.core.facade.AnalyzerFacade
import kpn.shared.Api
import kpn.shared.ApiResponse
import kpn.shared.ChangesPage
import kpn.shared.Fact
import kpn.shared.LocationPage
import kpn.shared.NetworkType
import kpn.shared.ReplicationId
import kpn.shared.Subset
import kpn.shared.changes.ChangeSetPage
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.network.NetworkChangesPage
import kpn.shared.network.NetworkDetailsPage
import kpn.shared.network.NetworkMapPage
import kpn.shared.network.NetworkNodesPage
import kpn.shared.network.NetworkRoutesPage
import kpn.shared.network.OldNetworkFactsPage
import kpn.shared.node.MapDetailNode
import kpn.shared.node.NodePage
import kpn.shared.route.MapDetailRoute
import kpn.shared.route.RoutePage
import kpn.shared.statistics.Statistics
import kpn.shared.subset.SubsetChangesPage
import kpn.shared.subset.SubsetFactDetailsPage
import kpn.shared.subset.SubsetFactsPage
import kpn.shared.subset.SubsetNetworksPage
import kpn.shared.subset.SubsetOrphanNodesPage
import kpn.shared.subset.SubsetOrphanRoutesPage

class ApiService(analyzerFacade: AnalyzerFacade, user: Option[String] = None)(implicit val system: ActorSystem) extends Api {

  override def overview(): ApiResponse[Statistics] = {
    analyzerFacade.overview(user)
  }

  override def subsetNetworks(subset: Subset): ApiResponse[SubsetNetworksPage] = {
    analyzerFacade.subsetNetworks(user, subset)
  }

  override def subsetFacts(subset: Subset): ApiResponse[SubsetFactsPage] = {
    analyzerFacade.subsetFacts(user, subset)
  }

  override def subsetFactDetails(subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage] = {
    analyzerFacade.subsetFactDetails(user, subset, fact)
  }

  override def subsetOrphanNodes(subset: Subset): ApiResponse[SubsetOrphanNodesPage] = {
    analyzerFacade.subsetOrphanNodes(user, subset)
  }

  override def subsetOrphanRoutes(subset: Subset): ApiResponse[SubsetOrphanRoutesPage] = {
    analyzerFacade.subsetOrphanRoutes(user, subset)
  }

  override def subsetChanges(parameters: ChangesParameters): ApiResponse[SubsetChangesPage] = {
    analyzerFacade.subsetChanges(user, parameters)
  }

  override def networkDetails(networkId: Long): ApiResponse[NetworkDetailsPage] = {
    analyzerFacade.networkDetails(user, networkId)
  }

  override def networkMap(networkId: Long): ApiResponse[NetworkMapPage] = {
    analyzerFacade.networkMap(user, networkId)
  }

  override def networkFacts(networkId: Long): ApiResponse[OldNetworkFactsPage] = {
    analyzerFacade.oldNetworkFacts(user, networkId)
  }

  override def networkNodes(networkId: Long): ApiResponse[NetworkNodesPage] = {
    analyzerFacade.networkNodes(user, networkId)
  }

  override def networkRoutes(networkId: Long): ApiResponse[NetworkRoutesPage] = {
    analyzerFacade.networkRoutes(user, networkId)
  }

  override def networkChanges(parameters: ChangesParameters): ApiResponse[NetworkChangesPage] = {
    analyzerFacade.networkChanges(user, parameters)
  }

  override def node(nodeId: Long): ApiResponse[NodePage] = {
    analyzerFacade.node(user, nodeId)
  }

  override def route(routeId: Long): ApiResponse[RoutePage] = {
    analyzerFacade.route(user, routeId)
  }

  override def changes(parameters: ChangesParameters): ApiResponse[ChangesPage] = {
    analyzerFacade.changes(user, parameters)
  }

  override def changeSet(changeSetId: Long, replicationNumber: Int): ApiResponse[ChangeSetPage] = {
    val replicationId = ReplicationId(replicationNumber)
    analyzerFacade.changeSet(user, changeSetId, Some(replicationId))
  }

  override def mapDetailNode(networkType: NetworkType, nodeId: Long): ApiResponse[MapDetailNode] = {
    analyzerFacade.mapDetailNode(user, networkType, nodeId)
  }

  override def mapDetailRoute(routeId: Long): ApiResponse[MapDetailRoute] = {
    analyzerFacade.mapDetailRoute(user, routeId)
  }

  override def location(networkType: NetworkType): ApiResponse[LocationPage] = {
    analyzerFacade.location(user, networkType)
  }

}
