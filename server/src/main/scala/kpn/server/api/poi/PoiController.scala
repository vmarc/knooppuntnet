package kpn.server.api.poi

import kpn.api.common.PoiDetail
import kpn.api.custom.ApiResponse
import kpn.server.analyzer.engine.poi.PoiRef
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class PoiController(poiFacade: PoiFacade) {

  @GetMapping(value = Array("/api/poi/areas"))
  def areas(): ApiResponse[String] = {
    poiFacade.areas()
  }

  @GetMapping(value = Array("/api/poi-detail/{elementType:node|way|relation}/{elementId}"))
  def getPoiDetail(
    @PathVariable elementType: String,
    @PathVariable elementId: Long
  ): ApiResponse[PoiDetail] = {
    poiFacade.getPoiDetail(PoiRef(elementType, elementId))
  }
}
