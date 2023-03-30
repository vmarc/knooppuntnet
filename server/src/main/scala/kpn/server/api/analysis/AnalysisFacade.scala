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

import javax.servlet.http.HttpServletRequest

trait AnalysisFacade {

  def nodeDetails(request: HttpServletRequest, user: Option[String], language: Language, nodeId: Long): ApiResponse[NodeDetailsPage]

  def nodeMap(request: HttpServletRequest, user: Option[String], nodeId: Long): ApiResponse[NodeMapPage]

  def nodeChanges(request: HttpServletRequest, user: Option[String], nodeId: Long, parameters: ChangesParameters): ApiResponse[NodeChangesPage]

  def routeDetails(request: HttpServletRequest, user: Option[String], language: Language, routeId: Long): ApiResponse[RouteDetailsPage]

  def routeMap(request: HttpServletRequest, user: Option[String], routeId: Long): ApiResponse[RouteMapPage]

  def routeChanges(request: HttpServletRequest, user: Option[String], routeId: Long, parameters: ChangesParameters): ApiResponse[RouteChangesPage]

  def subsetNetworks(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetNetworksPage]

  def subsetFacts(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetFactsPage]

  def subsetOrphanNodes(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetOrphanNodesPage]

  def subsetOrphanRoutes(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetOrphanRoutesPage]

  def subsetMap(request: HttpServletRequest, user: Option[String], subset: Subset): ApiResponse[SubsetMapPage]

  def subsetChanges(request: HttpServletRequest, user: Option[String], subset: Subset, parameters: ChangesParameters): ApiResponse[SubsetChangesPage]

  def networkDetails(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkDetailsPage]

  def networkMap(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkMapPage]

  def networkFacts(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkFactsPage]

  def networkNodes(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkNodesPage]

  def networkRoutes(request: HttpServletRequest, user: Option[String], networkId: Long): ApiResponse[NetworkRoutesPage]

  def networkChanges(request: HttpServletRequest, user: Option[String], networkId: Long, parameters: ChangesParameters): ApiResponse[NetworkChangesPage]

  def overview(request: HttpServletRequest, user: Option[String], language: Language): ApiResponse[Seq[StatisticValues]]

  def subsetFactDetails(request: HttpServletRequest, user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage]

  def subsetFactRefs(request: HttpServletRequest, user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactRefs]

  def changeSet(request: HttpServletRequest, user: Option[String], language: Language, changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage]

  def replication(request: HttpServletRequest, user: Option[String], language: Language, changeSetId: Long): ApiResponse[Long]

  def changes(request: HttpServletRequest, user: Option[String], language: Language, strategy: AnalysisStrategy, parameters: ChangesParameters): ApiResponse[ChangesPage]

  def locations(request: HttpServletRequest, user: Option[String], language: Language, networkType: NetworkType, country: Country): ApiResponse[LocationsPage]

  def locationNodes(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey, parameters: LocationNodesParameters): ApiResponse[LocationNodesPage]

  def locationRoutes(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey, parameters: LocationRoutesParameters): ApiResponse[LocationRoutesPage]

  def locationFacts(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey): ApiResponse[LocationFactsPage]

  def locationMap(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey): ApiResponse[LocationMapPage]

  def locationChanges(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey, parameters: LocationChangesParameters): ApiResponse[LocationChangesPage]

  def locationEdit(request: HttpServletRequest, user: Option[String], language: Language, locationKey: LocationKey): ApiResponse[LocationEditPage]

}
