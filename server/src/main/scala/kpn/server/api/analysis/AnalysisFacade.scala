package kpn.server.api.analysis

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
import kpn.api.custom.Statistics
import kpn.api.custom.Subset
import kpn.core.gpx.GpxFile

trait AnalysisFacade {

  def nodeDetails(user: Option[String], nodeId: Long): ApiResponse[NodeDetailsPage]

  def nodeMap(user: Option[String], nodeId: Long): ApiResponse[NodeMapPage]

  def nodeChanges(user: Option[String], nodeId: Long, parameters: ChangesParameters): ApiResponse[NodeChangesPage]

  def routeDetails(user: Option[String], routeId: Long): ApiResponse[RouteDetailsPage]

  def routeMap(user: Option[String], routeId: Long): ApiResponse[RouteMapPage]

  def routeChanges(user: Option[String], routeId: Long, parameters: ChangesParameters): ApiResponse[RouteChangesPage]

  def subsetNetworks(user: Option[String], subset: Subset): ApiResponse[SubsetNetworksPage]

  def subsetFacts(user: Option[String], subset: Subset): ApiResponse[SubsetFactsPage]

  def subsetOrphanNodes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanNodesPage]

  def subsetOrphanRoutes(user: Option[String], subset: Subset): ApiResponse[SubsetOrphanRoutesPage]

  def subsetMap(user: Option[String], subset: Subset): ApiResponse[SubsetMapPage]

  def subsetChanges(user: Option[String], subset: Subset, parameters: ChangesParameters): ApiResponse[SubsetChangesPage]

  def networkDetails(user: Option[String], networkId: Long): ApiResponse[NetworkDetailsPage]

  def networkMap(user: Option[String], networkId: Long): ApiResponse[NetworkMapPage]

  def networkFacts(user: Option[String], networkId: Long): ApiResponse[NetworkFactsPage]

  def networkNodes(user: Option[String], networkId: Long): ApiResponse[NetworkNodesPage]

  def networkRoutes(user: Option[String], networkId: Long): ApiResponse[NetworkRoutesPage]

  def networkChanges(user: Option[String], networkId: Long, parameters: ChangesParameters): ApiResponse[NetworkChangesPage]

  // TODO not used anymore? have to re-implement? cleanup?
  def gpx(user: Option[String], networkId: Long): Option[GpxFile]

  def overview(user: Option[String]): ApiResponse[Statistics]

  def subsetFactDetails(user: Option[String], subset: Subset, fact: Fact): ApiResponse[SubsetFactDetailsPage]

  def changeSet(user: Option[String], changeSetId: Long, replicationId: Option[ReplicationId]): ApiResponse[ChangeSetPage]

  def replication(user: Option[String], changeSetId: Long): ApiResponse[Long]

  def changes(user: Option[String], parameters: ChangesParameters): ApiResponse[ChangesPage]

  def locations(user: Option[String], language: Language, networkType: NetworkType, country: Country): ApiResponse[LocationsPage]

  def locationNodes(user: Option[String], locationKey: LocationKey, parameters: LocationNodesParameters): ApiResponse[LocationNodesPage]

  def locationRoutes(user: Option[String], locationKey: LocationKey, parameters: LocationRoutesParameters): ApiResponse[LocationRoutesPage]

  def locationFacts(user: Option[String], locationKey: LocationKey): ApiResponse[LocationFactsPage]

  def locationMap(user: Option[String], locationKey: LocationKey): ApiResponse[LocationMapPage]

  def locationChanges(user: Option[String], locationKey: LocationKey, parameters: LocationChangesParameters): ApiResponse[LocationChangesPage]

  def locationEdit(user: Option[String], locationKey: LocationKey): ApiResponse[LocationEditPage]

}
