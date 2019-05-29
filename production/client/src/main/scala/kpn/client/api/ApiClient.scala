// TODO migrate to Angular
package kpn.client.api

import autowire._
import kpn.client.services.AjaxClient
import kpn.shared.Api
import kpn.shared.ApiResponse
import kpn.shared.ChangesPage
import kpn.shared.Fact
import kpn.shared.NetworkType
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
import kpn.shared.subset.SubsetFactsPageNew
import kpn.shared.subset.SubsetNetworksPage
import kpn.shared.subset.SubsetOrphanNodesPage
import kpn.shared.subset.SubsetOrphanRoutesPage

import scala.concurrent.Future

// keep these imports, although IntelliJ thinks they are not used
import boopickle.DefaultBasic._
import kpn.shared.KpnPicklers._

import scala.concurrent.ExecutionContext.Implicits.global

object ApiClient {

  def overview(): Future[ApiResponse[Statistics]] = {
    AjaxClient[Api].overview().call()
  }

  def subsetNetworks(subset: Subset): Future[ApiResponse[SubsetNetworksPage]] = {
    AjaxClient[Api].subsetNetworks(subset).call()
  }

  def subsetFacts(subset: Subset): Future[ApiResponse[SubsetFactsPage]] = {
    AjaxClient[Api].subsetFacts(subset).call()
  }

  def subsetFactDetails(subset: Subset, fact: Fact): Future[ApiResponse[SubsetFactDetailsPage]] = {
    AjaxClient[Api].subsetFactDetails(subset, fact).call()
  }

  def subsetOrphanNodes(subset: Subset): Future[ApiResponse[SubsetOrphanNodesPage]]= {
    AjaxClient[Api].subsetOrphanNodes(subset).call()
  }

  def subsetOrphanRoutes(subset: Subset): Future[ApiResponse[SubsetOrphanRoutesPage]] = {
    AjaxClient[Api].subsetOrphanRoutes(subset).call()
  }

  def subsetChanges(parameters: ChangesParameters): Future[ApiResponse[SubsetChangesPage]] = {
    AjaxClient[Api].subsetChanges(parameters).call()
  }

  def networkDetails(networkId: Long): Future[ApiResponse[NetworkDetailsPage]] = {
    AjaxClient[Api].networkDetails(networkId).call()
  }

  def networkMap(networkId: Long): Future[ApiResponse[NetworkMapPage]] = {
    AjaxClient[Api].networkMap(networkId).call()
  }

  def networkFacts(networkId: Long): Future[ApiResponse[NetworkFactsPage]] = {
    AjaxClient[Api].networkFacts(networkId).call()
  }

  def networkNodes(networkId: Long): Future[ApiResponse[NetworkNodesPage]] = {
    AjaxClient[Api].networkNodes(networkId).call()
  }

  def networkRoutes(networkId: Long): Future[ApiResponse[NetworkRoutesPage]] = {
    AjaxClient[Api].networkRoutes(networkId).call()
  }

  def networkChanges(parameters: ChangesParameters): Future[ApiResponse[NetworkChangesPage]] = {
    AjaxClient[Api].networkChanges(parameters).call()
  }

  def node(nodeId: Long): Future[ApiResponse[NodePage]] = {
    AjaxClient[Api].node(nodeId).call()
  }

  def route(routeId: Long): Future[ApiResponse[RoutePage]] = {
    AjaxClient[Api].route(routeId).call()
  }

  def changes(parameters: ChangesParameters): Future[ApiResponse[ChangesPage]] = {
    AjaxClient[Api].changes(parameters).call()
  }

  def changeSet(changeSetId: Long, replicationNumber: Int): Future[ApiResponse[ChangeSetPage]] = {
    AjaxClient[Api].changeSet(changeSetId, replicationNumber).call()
  }

  def mapDetailNode(networkType: NetworkType, nodeId: Long): Future[ApiResponse[MapDetailNode]] = {
    AjaxClient[Api].mapDetailNode(networkType, nodeId).call()
  }

  def mapDetailRoute(routeId: Long): Future[ApiResponse[MapDetailRoute]] = {
    AjaxClient[Api].mapDetailRoute(routeId).call()
  }

}
