package kpn.server.api.analysis

import kpn.api.common.ChangesPage
import kpn.api.common.PoiPage
import kpn.api.common.ReplicationId
import kpn.api.common.SurveyDateInfo
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
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AnalysisController(analysisFacade: AnalysisFacade) {

  @GetMapping(value = Array("/json-api/overview"))
  def overview(): ApiResponse[Statistics] = {
    analysisFacade.overview(user())
  }

  @GetMapping(value = Array("/json-api/{country:be|de|fr|nl|at}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/networks"))
  def subsetNetworks(
    @PathVariable country: String,
    @PathVariable networkType: String
  ): ApiResponse[SubsetNetworksPage] = {
    val subset = Subset.ofName(country, networkType)
    analysisFacade.subsetNetworks(user(), subset.get)
  }

  @GetMapping(value = Array("/json-api/{country:be|de|fr|nl|at}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/facts"))
  def subsetFacts(
    @PathVariable country: String,
    @PathVariable networkType: String
  ): ApiResponse[SubsetFactsPage] = {
    val subset = Subset.ofName(country, networkType)
    analysisFacade.subsetFacts(user(), subset.get)
  }

  @GetMapping(value = Array("/json-api/{country:be|de|fr|nl|at}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/{fact}"))
  def subsetFactDetails(
    @PathVariable country: String,
    @PathVariable networkType: String,
    @PathVariable fact: String
  ): ApiResponse[SubsetFactDetailsPage] = {
    val subset = Subset.ofName(country, networkType).get // TODO improve
    val f = Fact.withName(fact).get // TODO improve
    analysisFacade.subsetFactDetails(user(), subset, f)
  }

  @GetMapping(value = Array("/json-api/{country:be|de|fr|nl|at}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/orphan-nodes"))
  def subsetOrphanNodes(
    @PathVariable country: String,
    @PathVariable networkType: String
  ): ApiResponse[SubsetOrphanNodesPage] = {
    val subset = Subset.ofName(country, networkType)
    analysisFacade.subsetOrphanNodes(user(), subset.get)
  }

  @GetMapping(value = Array("/json-api/{country:be|de|fr|nl|at}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/orphan-routes"))
  def subsetOrphanRoutes(
    @PathVariable country: String,
    @PathVariable networkType: String
  ): ApiResponse[SubsetOrphanRoutesPage] = {
    val subset = Subset.ofName(country, networkType)
    analysisFacade.subsetOrphanRoutes(user(), subset.get)
  }

  @GetMapping(value = Array("/json-api/{country:be|de|fr|nl|at}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/map"))
  def subsetMap(
    @PathVariable country: String,
    @PathVariable networkType: String
  ): ApiResponse[SubsetMapPage] = {
    val subset = Subset.ofName(country, networkType)
    analysisFacade.subsetMap(user(), subset.get)
  }

  @PostMapping(value = Array("/json-api/{country:be|de|fr|nl|at}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/changes"))
  def subsetChanges(
    @PathVariable country: String,
    @PathVariable networkType: String,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[SubsetChangesPage] = {
    val p = parameters.copy(subset = Subset.ofName(country, networkType))
    analysisFacade.subsetChanges(user(), p)
  }

  @GetMapping(value = Array("/json-api/network/{networkId}"))
  def networkDetails(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkDetailsPage] = {
    analysisFacade.networkDetails(user(), networkId)
  }

  @GetMapping(value = Array("/json-api/network/{networkId}/map"))
  def networkMap(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkMapPage] = {
    analysisFacade.networkMap(user(), networkId)
  }

  @GetMapping(value = Array("/json-api/network/{networkId}/facts"))
  def networkFacts(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkFactsPage] = {
    analysisFacade.networkFacts(user(), networkId)
  }

  @GetMapping(value = Array("/json-api/network/{networkId}/nodes"))
  def networkNodes(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkNodesPage] = {
    analysisFacade.networkNodes(user(), networkId)
  }

  @GetMapping(value = Array("/json-api/network/{networkId}/routes"))
  def networkRoutes(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkRoutesPage] = {
    analysisFacade.networkRoutes(user(), networkId)
  }

  @PostMapping(value = Array("/json-api/network/{networkId}/changes"))
  def networkChanges(
    @PathVariable networkId: Long,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[NetworkChangesPage] = {
    val p = parameters.copy(networkId = Some(networkId))
    analysisFacade.networkChanges(user(), p)
  }

  @GetMapping(value = Array("/json-api/node/{nodeId}"))
  def node(
    @PathVariable nodeId: Long
  ): ApiResponse[NodeDetailsPage] = {
    analysisFacade.nodeDetails(user(), nodeId)
  }

  @GetMapping(value = Array("/json-api/node/{nodeId}/map"))
  def nodeMap(
    @PathVariable nodeId: Long
  ): ApiResponse[NodeMapPage] = {
    analysisFacade.nodeMap(user(), nodeId)
  }

  @PostMapping(value = Array("/json-api/node/{nodeId}/changes"))
  def nodeChanges(
    @PathVariable nodeId: Long,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[NodeChangesPage] = {
    val p = parameters.copy(nodeId = Some(nodeId))
    analysisFacade.nodeChanges(user(), nodeId, p)
  }

  @GetMapping(value = Array("/json-api/route/{routeId}"))
  def route(
    @PathVariable routeId: Long
  ): ApiResponse[RouteDetailsPage] = {
    analysisFacade.routeDetails(user(), routeId)
  }

  @GetMapping(value = Array("/json-api/route/{routeId}/map"))
  def routeMap(
    @PathVariable routeId: Long
  ): ApiResponse[RouteMapPage] = {
    analysisFacade.routeMap(user(), routeId)
  }

  @PostMapping(value = Array("/json-api/route/{routeId}/changes"))
  def routeChanges(
    @PathVariable routeId: Long,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[RouteChangesPage] = {
    val p = parameters.copy(routeId = Some(routeId))
    analysisFacade.routeChanges(user(), routeId, p)
  }

  @PostMapping(value = Array("/json-api/changes"))
  def changes(
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[ChangesPage] = {
    analysisFacade.changes(user(), parameters)
  }

  @GetMapping(value = Array("/json-api/changeset/{changeSetId}/{replicationNumber}"))
  def changeSet(
    @PathVariable changeSetId: Long,
    @PathVariable replicationNumber: Int
  ): ApiResponse[ChangeSetPage] = {
    val replicationId = ReplicationId(replicationNumber)
    analysisFacade.changeSet(user(), changeSetId, Some(replicationId))
  }

  @GetMapping(value = Array("/json-api/node-detail/{nodeId}/{networkType}"))
  def mapNodeDetail(
    @PathVariable networkType: String,
    @PathVariable nodeId: Long
  ): ApiResponse[MapNodeDetail] = {
    val networkTypeValue = NetworkType.withName(networkType).get
    analysisFacade.mapNodeDetail(user(), networkTypeValue, nodeId)
  }

  @GetMapping(value = Array("/json-api/route-detail/{routeId}"))
  def mapRouteDetail(
    @PathVariable routeId: Long
  ): ApiResponse[MapRouteDetail] = {
    analysisFacade.mapRouteDetail(user(), routeId)
  }

  @GetMapping(value = Array("/json-api/poi-configuration"))
  def poiConfiguration(
  ): ApiResponse[ClientPoiConfiguration] = {
    analysisFacade.poiConfiguration(user())
  }

  @GetMapping(value = Array("/json-api/poi/{elementType}/{elementId}"))
  def poi(
    @PathVariable elementType: String,
    @PathVariable elementId: Long
  ): ApiResponse[PoiPage] = {
    analysisFacade.poi(user(), PoiRef(elementType, elementId))
  }

  @GetMapping(value = Array("/json-api/leg/{networkType}/{legId}/{sourceNodeId}/{sinkNodeId}"))
  def leg(
    @PathVariable networkType: String,
    @PathVariable legId: String,
    @PathVariable sourceNodeId: String,
    @PathVariable sinkNodeId: String
  ): ApiResponse[RouteLeg] = {
    analysisFacade.leg(user(), NetworkType.withName(networkType).get, legId, sourceNodeId, sinkNodeId)
  }

  @GetMapping(value = Array("/json-api/survey-date-info"))
  def surveyDateInfo(): ApiResponse[SurveyDateInfo] = {
    ApiResponse(None, 1, Some(SurveyDateInfoBuilder.dateInfo))
  }

  @GetMapping(value = Array("/json-api/locations/{networkType}/{country}"))
  def locations(
    @PathVariable networkType: String,
    @PathVariable country: String
  ): ApiResponse[LocationsPage] = {
    analysisFacade.locations(user(), NetworkType.withName(networkType).get, Country.withDomain(country).get)
  }

  @PostMapping(value = Array("/json-api/{networkType}/{country}/{location}/nodes"))
  def locationNodes(
    @PathVariable networkType: String,
    @PathVariable country: String,
    @PathVariable location: String,
    @RequestBody parameters: LocationNodesParameters
  ): ApiResponse[LocationNodesPage] = {
    val locationKey = LocationKey(
      NetworkType.withName(networkType).get,
      Country.withDomain(country).get,
      location
    )
    analysisFacade.locationNodes(user(), locationKey, parameters)
  }

  @PostMapping(value = Array("/json-api/{networkType}/{country}/{location}/routes"))
  def locationRoutes(
    @PathVariable networkType: String,
    @PathVariable country: String,
    @PathVariable location: String,
    @RequestBody parameters: LocationRoutesParameters
  ): ApiResponse[LocationRoutesPage] = {
    val locationKey = LocationKey(
      NetworkType.withName(networkType).get,
      Country.withDomain(country).get,
      location
    )
    analysisFacade.locationRoutes(user(), locationKey, parameters)
  }

  @GetMapping(value = Array("/json-api/{networkType}/{country}/{location}/facts"))
  def locationFacts(
    @PathVariable networkType: String,
    @PathVariable country: String,
    @PathVariable location: String
  ): ApiResponse[LocationFactsPage] = {
    val locationKey = LocationKey(
      NetworkType.withName(networkType).get,
      Country.withDomain(country).get,
      location
    )
    analysisFacade.locationFacts(user(), locationKey)
  }

  @GetMapping(value = Array("/json-api/{networkType}/{country}/{location}/map"))
  def locationMap(
    @PathVariable networkType: String,
    @PathVariable country: String,
    @PathVariable location: String
  ): ApiResponse[LocationMapPage] = {
    val locationKey = LocationKey(
      NetworkType.withName(networkType).get,
      Country.withDomain(country).get,
      location
    )
    analysisFacade.locationMap(user(), locationKey)
  }

  @PostMapping(value = Array("/json-api/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/{country:be|de|fr|nl|at}/{location}/changes"))
  def locationChanges(
    @PathVariable country: String,
    @PathVariable networkType: String,
    @PathVariable location: String,
    @RequestBody parameters: LocationChangesParameters
  ): ApiResponse[LocationChangesPage] = {
    val locationKey = LocationKey(
      NetworkType.withName(networkType).get,
      Country.withDomain(country).get,
      location
    )
    analysisFacade.locationChanges(user(), locationKey, parameters)
  }

  private def user(): Option[String] = {
    val authentication = SecurityContextHolder.getContext.getAuthentication
    if (authentication != null) {
      Some(authentication.getName)
    }
    else {
      None
    }
  }
}
