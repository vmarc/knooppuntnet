package kpn.server.api.status

import kpn.api.common.status.BarChart
import kpn.api.common.status.BarChart2D
import kpn.api.custom.ApiResponse

trait StatusFacade {

  def example(): ApiResponse[BarChart2D]

  def dayAction(action: String): ApiResponse[BarChart]

  def dayActionAverage(action: String): ApiResponse[BarChart]

  def dayDelay(): ApiResponse[BarChart2D]
}
