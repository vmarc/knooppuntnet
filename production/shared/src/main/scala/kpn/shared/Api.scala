package kpn.shared

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

trait Api {

  def overview(): ApiResponse[Statistics]

  def subsetNetworks(context: Subset): ApiResponse[SubsetNetworksPage]

  def subsetFacts(context: Subset): ApiResponse[SubsetFactsPage]

  def subsetFactDetails(context: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage]

  def subsetOrphanNodes(context: Subset): ApiResponse[SubsetOrphanNodesPage]

  def subsetOrphanRoutes(context: Subset): ApiResponse[SubsetOrphanRoutesPage]

  def subsetChanges(parameters: ChangesParameters): ApiResponse[SubsetChangesPage]

  def networkDetails(networkId: Long): ApiResponse[NetworkDetailsPage]

  def networkMap(networkId: Long): ApiResponse[NetworkMapPage]

  def networkFacts(networkId: Long): ApiResponse[NetworkFactsPage]

  def networkNodes(networkId: Long): ApiResponse[NetworkNodesPage]

  def networkRoutes(networkId: Long): ApiResponse[NetworkRoutesPage]

  def networkChanges(parameters: ChangesParameters): ApiResponse[NetworkChangesPage]

  def node(nodeId: Long): ApiResponse[NodePage]

  def route(routeId: Long): ApiResponse[RoutePage]

  def changes(parameters: ChangesParameters): ApiResponse[ChangesPage]

  def changeSet(changeSetId: Long, replicationNumber: Int): ApiResponse[ChangeSetPage]

  def mapDetailNode(networkType: NetworkType, nodeId: Long): ApiResponse[MapDetailNode]

  def mapDetailRoute(routeId: Long): ApiResponse[MapDetailRoute]

}
