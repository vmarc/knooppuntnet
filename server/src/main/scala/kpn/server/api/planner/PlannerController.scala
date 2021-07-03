package kpn.server.api.planner

import kpn.api.common.PoiPage
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.PlanLegDetail
import kpn.api.common.planner.PlanParams
import kpn.api.common.route.MapRouteDetail
import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.api.custom.ApiResponse
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.CurrentUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PlannerController(plannerFacade: PlannerFacade) {

  @GetMapping(value = Array("/api/node-detail/{nodeId}/{networkType}"))
  def mapNodeDetail(
    @PathVariable networkType: String,
    @PathVariable nodeId: Long
  ): ApiResponse[MapNodeDetail] = {
    val networkTypeValue = NetworkType.withName(networkType).get
    plannerFacade.mapNodeDetail(CurrentUser.name, networkTypeValue, nodeId)
  }

  @GetMapping(value = Array("/api/route-detail/{routeId}"))
  def mapRouteDetail(
    @PathVariable routeId: Long
  ): ApiResponse[MapRouteDetail] = {
    plannerFacade.mapRouteDetail(CurrentUser.name, routeId)
  }

  @GetMapping(value = Array("/api/poi-configuration"))
  def poiConfiguration(
  ): ApiResponse[ClientPoiConfiguration] = {
    plannerFacade.poiConfiguration(CurrentUser.name)
  }

  @GetMapping(value = Array("/api/poi/{elementType}/{elementId}"))
  def poi(
    @PathVariable elementType: String,
    @PathVariable elementId: Long
  ): ApiResponse[PoiPage] = {
    plannerFacade.poi(CurrentUser.name, PoiRef(elementType, elementId))
  }

  @PostMapping(path = Array("/api/leg"), consumes = Array("application/json"))
  def leg(@RequestBody params: LegBuildParams): ApiResponse[PlanLegDetail] = {
    plannerFacade.leg(CurrentUser.name, params)
  }

  @PostMapping(path = Array("/api/plan"), consumes = Array("application/json"))
  def plan(@RequestBody params: PlanParams): ApiResponse[PlanLegDetail] = {
    plannerFacade.plan(CurrentUser.name, NetworkType.withName(params.networkType).get, params.planString, proposed = false)
  }

}
