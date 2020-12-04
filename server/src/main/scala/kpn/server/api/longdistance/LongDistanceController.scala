package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRoute
import kpn.api.custom.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class LongDistanceController(facade: LongDistanceFacade) {

  @GetMapping(value = Array("/json-api/long-distance/routes"))
  def routes(): ApiResponse[Seq[LongDistanceRoute]] = {
    facade.routes()
  }

  @GetMapping(value = Array("/json-api/long-distance/routes/{routeId}"))
  def route(
    @PathVariable routeId: Long
  ): ApiResponse[LongDistanceRoute] = {
    facade.route(routeId)
  }
}
