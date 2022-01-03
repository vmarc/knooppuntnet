package kpn.server.api.analysis

import kpn.api.common.AnalysisStrategy
import kpn.api.common.ChangesPage
import kpn.api.common.EN
import kpn.api.common.LOCATION
import kpn.api.common.Language
import kpn.api.common.Languages
import kpn.api.common.ReplicationId
import kpn.api.common.SurveyDateInfo
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
import kpn.server.api.CurrentUser
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AnalysisController(analysisFacade: AnalysisFacade) {

  @GetMapping(value = Array("/api/overview"))
  def overview(): ApiResponse[Seq[StatisticValues]] = {
    analysisFacade.overview(CurrentUser.name)
  }

  @GetMapping(value = Array("/api/{country:be|de|fr|nl|at|es}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/networks"))
  def subsetNetworks(
    @PathVariable country: Country,
    @PathVariable networkType: NetworkType
  ): ApiResponse[SubsetNetworksPage] = {
    val subset = Subset.of(country, networkType)
    analysisFacade.subsetNetworks(CurrentUser.name, subset.get)
  }

  @GetMapping(value = Array("/api/{country:be|de|fr|nl|at|es}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/facts"))
  def subsetFacts(
    @PathVariable country: Country,
    @PathVariable networkType: NetworkType
  ): ApiResponse[SubsetFactsPage] = {
    val subset = Subset.of(country, networkType)
    analysisFacade.subsetFacts(CurrentUser.name, subset.get)
  }

  @GetMapping(value = Array("/api/{country:be|de|fr|nl|at|es}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/{fact}"))
  def subsetFactDetails(
    @PathVariable country: Country,
    @PathVariable networkType: NetworkType,
    @PathVariable fact: String
  ): ApiResponse[SubsetFactDetailsPage] = {
    val subset = Subset.of(country, networkType).get // TODO improve
    val f = Fact.withName(fact).get // TODO improve
    analysisFacade.subsetFactDetails(CurrentUser.name, subset, f)
  }

  @GetMapping(value = Array("/api/{country:be|de|fr|nl|at|es}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/orphan-nodes"))
  def subsetOrphanNodes(
    @PathVariable country: Country,
    @PathVariable networkType: NetworkType
  ): ApiResponse[SubsetOrphanNodesPage] = {
    val subset = Subset.of(country, networkType)
    analysisFacade.subsetOrphanNodes(CurrentUser.name, subset.get)
  }

  @GetMapping(value = Array("/api/{country:be|de|fr|nl|at|es}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/orphan-routes"))
  def subsetOrphanRoutes(
    @PathVariable country: Country,
    @PathVariable networkType: NetworkType
  ): ApiResponse[SubsetOrphanRoutesPage] = {
    val subset = Subset.of(country, networkType)
    analysisFacade.subsetOrphanRoutes(CurrentUser.name, subset.get)
  }

  @GetMapping(value = Array("/api/{country:be|de|fr|nl|at|es}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/map"))
  def subsetMap(
    @PathVariable country: Country,
    @PathVariable networkType: NetworkType
  ): ApiResponse[SubsetMapPage] = {
    val subset = Subset.of(country, networkType)
    analysisFacade.subsetMap(CurrentUser.name, subset.get)
  }

  @PostMapping(value = Array("/api/{country:be|de|fr|nl|at|es}/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/changes"))
  def subsetChanges(
    @PathVariable country: Country,
    @PathVariable networkType: NetworkType,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[SubsetChangesPage] = {
    val subset = Subset.of(country, networkType)
    analysisFacade.subsetChanges(
      CurrentUser.name,
      subset.get,
      parameters
    )
  }

  @GetMapping(value = Array("/api/network/{networkId}"))
  def networkDetails(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkDetailsPage] = {
    analysisFacade.networkDetails(CurrentUser.name, networkId)
  }

  @GetMapping(value = Array("/api/network/{networkId}/map"))
  def networkMap(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkMapPage] = {
    analysisFacade.networkMap(CurrentUser.name, networkId)
  }

  @GetMapping(value = Array("/api/network/{networkId}/facts"))
  def networkFacts(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkFactsPage] = {
    analysisFacade.networkFacts(CurrentUser.name, networkId)
  }

  @GetMapping(value = Array("/api/network/{networkId}/nodes"))
  def networkNodes(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkNodesPage] = {
    analysisFacade.networkNodes(CurrentUser.name, networkId)
  }

  @GetMapping(value = Array("/api/network/{networkId}/routes"))
  def networkRoutes(
    @PathVariable networkId: Long
  ): ApiResponse[NetworkRoutesPage] = {
    analysisFacade.networkRoutes(CurrentUser.name, networkId)
  }

  @PostMapping(value = Array("/api/network/{networkId}/changes"))
  def networkChanges(
    @PathVariable networkId: Long,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[NetworkChangesPage] = {
    analysisFacade.networkChanges(CurrentUser.name, networkId, parameters)
  }

  @GetMapping(value = Array("/api/node/{nodeId}"))
  def node(
    @RequestParam language: String,
    @PathVariable nodeId: Long
  ): ApiResponse[NodeDetailsPage] = {
    analysisFacade.nodeDetails(CurrentUser.name, toLanguage(language), nodeId)
  }

  @GetMapping(value = Array("/api/node/{nodeId}/map"))
  def nodeMap(
    @PathVariable nodeId: Long
  ): ApiResponse[NodeMapPage] = {
    analysisFacade.nodeMap(CurrentUser.name, nodeId)
  }

  @PostMapping(value = Array("/api/node/{nodeId}/changes"))
  def nodeChanges(
    @PathVariable nodeId: Long,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[NodeChangesPage] = {
    analysisFacade.nodeChanges(CurrentUser.name, nodeId, parameters)
  }

  @GetMapping(value = Array("/api/route/{routeId}"))
  def route(
    @RequestParam language: String,
    @PathVariable routeId: Long
  ): ApiResponse[RouteDetailsPage] = {
    analysisFacade.routeDetails(CurrentUser.name, toLanguage(language), routeId)
  }

  @GetMapping(value = Array("/api/route/{routeId}/map"))
  def routeMap(
    @PathVariable routeId: Long
  ): ApiResponse[RouteMapPage] = {
    analysisFacade.routeMap(CurrentUser.name, routeId)
  }

  @PostMapping(value = Array("/api/route/{routeId}/changes"))
  def routeChanges(
    @PathVariable routeId: Long,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[RouteChangesPage] = {
    analysisFacade.routeChanges(CurrentUser.name, routeId, parameters)
  }

  @PostMapping(value = Array("/api/changes"))
  def changes(
    @RequestParam language: String,
    @RequestParam strategy: String,
    @RequestBody parameters: ChangesParameters
  ): ApiResponse[ChangesPage] = {
    analysisFacade.changes(
      CurrentUser.name,
      toLanguage(language),
      toAnalysisStrategy(strategy),
      parameters
    )
  }

  @GetMapping(value = Array("/api/changeset/{changeSetId}/{replicationNumber}"))
  def changeSet(
    @RequestParam language: String,
    @PathVariable changeSetId: Long,
    @PathVariable replicationNumber: Int
  ): ApiResponse[ChangeSetPage] = {
    val replicationId = ReplicationId(replicationNumber)
    analysisFacade.changeSet(CurrentUser.name, toLanguage(language), changeSetId, Some(replicationId))
  }

  @GetMapping(value = Array("/api/replication/{changeSetId}"))
  def replication(
    @RequestParam language: String,
    @PathVariable changeSetId: Long
  ): ApiResponse[Long] = {
    analysisFacade.replication(CurrentUser.name, toLanguage(language), changeSetId)
  }

  @GetMapping(value = Array("/api/survey-date-info"))
  def surveyDateInfo(): ApiResponse[SurveyDateInfo] = {
    ApiResponse(None, 1, Some(SurveyDateInfoBuilder.dateInfo))
  }

  @GetMapping(value = Array("/api/locations/{language}/{networkType}/{country}"))
  def locations(
    @PathVariable language: String,
    @PathVariable networkType: NetworkType,
    @PathVariable country: Country
  ): ApiResponse[LocationsPage] = {
    analysisFacade.locations(CurrentUser.name, toLanguage(language), networkType, country)
  }

  @PostMapping(value = Array("/api/{networkType}/{country}/{location}/nodes"))
  def locationNodes(
    @RequestParam language: String,
    @PathVariable networkType: NetworkType,
    @PathVariable country: Country,
    @PathVariable location: String,
    @RequestBody parameters: LocationNodesParameters
  ): ApiResponse[LocationNodesPage] = {
    val locationKey = LocationKey(networkType, country, location)
    analysisFacade.locationNodes(CurrentUser.name, toLanguage(language), locationKey, parameters)
  }

  @PostMapping(value = Array("/api/{networkType}/{country}/{location}/routes"))
  def locationRoutes(
    @RequestParam language: String,
    @PathVariable networkType: NetworkType,
    @PathVariable country: Country,
    @PathVariable location: String,
    @RequestBody parameters: LocationRoutesParameters
  ): ApiResponse[LocationRoutesPage] = {
    val locationKey = LocationKey(networkType, country, location)
    analysisFacade.locationRoutes(CurrentUser.name, toLanguage(language), locationKey, parameters)
  }

  @GetMapping(value = Array("/api/{networkType}/{country}/{location}/facts"))
  def locationFacts(
    @RequestParam language: String,
    @PathVariable networkType: NetworkType,
    @PathVariable country: Country,
    @PathVariable location: String
  ): ApiResponse[LocationFactsPage] = {
    val locationKey = LocationKey(networkType, country, location)
    analysisFacade.locationFacts(CurrentUser.name, toLanguage(language), locationKey)
  }

  @GetMapping(value = Array("/api/{networkType}/{country}/{location}/map"))
  def locationMap(
    @RequestParam language: String,
    @PathVariable networkType: NetworkType,
    @PathVariable country: Country,
    @PathVariable location: String
  ): ApiResponse[LocationMapPage] = {
    val locationKey = LocationKey(networkType, country, location)
    analysisFacade.locationMap(CurrentUser.name, toLanguage(language), locationKey)
  }

  @PostMapping(value = Array("/api/{networkType:cycling|hiking|horse-riding|motorboat|canoe|inline-skating}/{country:be|de|fr|nl|at|es}/{location}/changes"))
  def locationChanges(
    @RequestParam language: String,
    @PathVariable networkType: NetworkType,
    @PathVariable country: Country,
    @PathVariable location: String,
    @RequestBody parameters: LocationChangesParameters
  ): ApiResponse[LocationChangesPage] = {
    val locationKey = LocationKey(networkType, country, location)
    analysisFacade.locationChanges(CurrentUser.name, toLanguage(language), locationKey, parameters)
  }

  @PostMapping(value = Array("/api/{networkType}/{country}/{location}/edit"))
  def locationEdit(
    @RequestParam language: String,
    @PathVariable networkType: NetworkType,
    @PathVariable country: Country,
    @PathVariable location: String
  ): ApiResponse[LocationEditPage] = {
    val locationKey = LocationKey(networkType, country, location)
    analysisFacade.locationEdit(CurrentUser.name, toLanguage(language), locationKey)
  }

  private def toLanguage(language: String): Language = {
    Languages.all.find(_.toString.toLowerCase == language).getOrElse(EN)
  }

  private def toAnalysisStrategy(analysisStrategy: String): AnalysisStrategy = {
    AnalysisStrategy.all.find(_.toString.toLowerCase == analysisStrategy).getOrElse(LOCATION)
  }
}
