package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangesPage
import kpn.api.common.longdistance.LongDistanceRouteDetailsPage
import kpn.api.common.longdistance.LongDistanceRouteMapPage
import kpn.api.common.longdistance.LongDistanceRoutesPage
import kpn.api.custom.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class LongDistanceController(facade: LongDistanceFacade) {

  @GetMapping(value = Array("/json-api/long-distance/routes"))
  def routes(): ApiResponse[LongDistanceRoutesPage] = {
    facade.routes()
  }

  @GetMapping(value = Array("/json-api/long-distance/routes/{routeId}"))
  def route(
    @PathVariable routeId: Long
  ): ApiResponse[LongDistanceRouteDetailsPage] = {
    facade.route(routeId)
  }

  @GetMapping(value = Array("/json-api/long-distance/routes/{routeId}/map"))
  def routeMap(
    @PathVariable routeId: Long
  ): ApiResponse[LongDistanceRouteMapPage] = {
    facade.routeMap(routeId)
  }

  @GetMapping(value = Array("/json-api/long-distance/routes/{routeId}/changes"))
  def routeChanges(
    @PathVariable routeId: Long
  ): ApiResponse[LongDistanceRouteChangesPage] = {
    facade.routeChanges(routeId)
  }
}
