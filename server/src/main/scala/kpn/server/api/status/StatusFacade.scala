package kpn.server.api.status

import kpn.api.common.status.BarChart2D
import kpn.api.custom.ApiResponse

trait StatusFacade {

  def example(): ApiResponse[BarChart2D]

}
