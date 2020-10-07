package kpn.server.api.poi

import kpn.api.custom.ApiResponse

trait PoiFacade {
  def areas(): ApiResponse[String]
}
