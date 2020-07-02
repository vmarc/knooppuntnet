package kpn.server.api.planner

import kpn.api.common.PoiPage
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.Plan
import kpn.api.common.planner.PlanLeg
import kpn.api.common.planner.PlanParams
import kpn.api.common.route.MapRouteDetail
import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.api.custom.ApiResponse
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.poi.PoiRef
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PlannerController(plannerFacade: PlannerFacade) {

  @GetMapping(value = Array("/json-api/node-detail/{nodeId}/{networkType}"))
  def mapNodeDetail(
    @PathVariable networkType: String,
    @PathVariable nodeId: Long
  ): ApiResponse[MapNodeDetail] = {
    val networkTypeValue = NetworkType.withName(networkType).get
    plannerFacade.mapNodeDetail(user(), networkTypeValue, nodeId)
  }

  @GetMapping(value = Array("/json-api/route-detail/{routeId}"))
  def mapRouteDetail(
    @PathVariable routeId: Long
  ): ApiResponse[MapRouteDetail] = {
    plannerFacade.mapRouteDetail(user(), routeId)
  }

  @GetMapping(value = Array("/json-api/poi-configuration"))
  def poiConfiguration(
  ): ApiResponse[ClientPoiConfiguration] = {
    plannerFacade.poiConfiguration(user())
  }

  @GetMapping(value = Array("/json-api/poi/{elementType}/{elementId}"))
  def poi(
    @PathVariable elementType: String,
    @PathVariable elementId: Long
  ): ApiResponse[PoiPage] = {
    plannerFacade.poi(user(), PoiRef(elementType, elementId))
  }

  @PostMapping(path = Array("/json-api/leg"), consumes = Array("application/json"))
  def leg(@RequestBody params: LegBuildParams): ApiResponse[PlanLeg] = {
    plannerFacade.leg(user(), params)
  }

  @PostMapping(path = Array("/json-api/plan"), consumes = Array("application/json"))
  def plan(@RequestBody params: PlanParams): ApiResponse[Plan] = {
    plannerFacade.plan(user(), NetworkType.withName(params.networkType).get, params.planString)
  }

  // TODO share with other controllers
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
