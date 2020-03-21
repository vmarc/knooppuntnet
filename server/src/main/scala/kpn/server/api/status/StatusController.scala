package kpn.server.api.status

import kpn.api.common.status.BarChart2D
import kpn.api.custom.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StatusController(statusFacade: StatusFacade) {

  @GetMapping(value = Array("/json-api/status/example"))
  def example(): ApiResponse[BarChart2D] = {
    statusFacade.example()
  }

}
