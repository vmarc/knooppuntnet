package kpn.server.api.poi

import kpn.api.custom.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PoiController(poiFacade: PoiFacade) {

  @GetMapping(value = Array("/json-api/poi/areas"))
  def areas(): ApiResponse[String] = {
    poiFacade.areas()
  }

}
