package kpn.server.api.planner

import kpn.api.common.PoiPage
import kpn.api.common.node.MapNodeDetail
import kpn.api.common.planner.RouteLeg
import kpn.api.common.route.MapRouteDetail
import kpn.api.common.tiles.ClientPoiConfiguration
import kpn.api.custom.ApiResponse
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.planner.leg.LegBuildParams
import kpn.server.api.planner.leg.ViaRoute
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

  @GetMapping(value = Array("/json-api/leg/{networkType}/{legId}/{sourceNodeId}/{sinkNodeId}"))
  def leg(
    @PathVariable networkType: String,
    @PathVariable legId: String,
    @PathVariable sourceNodeId: String,
    @PathVariable sinkNodeId: String
  ): ApiResponse[RouteLeg] = {
    val params = LegBuildParams(
      NetworkType.withName(networkType).get,
      legId,
      sourceNodeId.toLong,
      sinkNodeId.toLong,
      None
    )
    plannerFacade.leg(user(), params)
  }

  @GetMapping(value = Array("/json-api/leg-via-route/{networkType}/{legId}/{sourceNodeId}/{sinkNodeId}/{routeId}/{pathId}"))
  def legViaRoute(
    @PathVariable networkType: String,
    @PathVariable legId: String,
    @PathVariable sourceNodeId: String,
    @PathVariable sinkNodeId: String,
    @PathVariable routeId: String,
    @PathVariable pathId: String
  ): ApiResponse[RouteLeg] = {
    val params = LegBuildParams(
      NetworkType.withName(networkType).get,
      legId,
      sourceNodeId.toLong,
      sinkNodeId.toLong,
      Some(
        ViaRoute(
          routeId.toLong,
          pathId.toLong
        )
      )
    )
    plannerFacade.leg(user(), params)
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
