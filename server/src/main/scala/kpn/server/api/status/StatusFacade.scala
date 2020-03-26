package kpn.server.api.status

import kpn.api.common.status.BarChart
import kpn.api.common.status.BarChart2D
import kpn.api.common.status.PeriodParameters
import kpn.api.common.status.ReplicationStatusPage
import kpn.api.common.status.Status
import kpn.api.common.status.SystemStatusPage
import kpn.api.custom.ApiResponse

trait StatusFacade {

  def status(): ApiResponse[Status]

  def replicationStatus(parameters: PeriodParameters): ApiResponse[ReplicationStatusPage]

  def systemStatus(parameters: PeriodParameters): ApiResponse[SystemStatusPage]

  def dayAction(action: String): ApiResponse[BarChart]

  def dayActionAverage(action: String): ApiResponse[BarChart]

  def dayDelay(): ApiResponse[BarChart2D]

  def example(): ApiResponse[BarChart2D]

}
