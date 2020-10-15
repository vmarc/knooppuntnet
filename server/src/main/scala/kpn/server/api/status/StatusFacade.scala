package kpn.server.api.status

import kpn.api.common.status.PeriodParameters
import kpn.api.common.status.ReplicationStatusPage
import kpn.api.common.status.Status
import kpn.api.common.status.SystemStatusPage
import kpn.api.custom.ApiResponse

trait StatusFacade {

  def status(): ApiResponse[Status]

  def statusOk(): ApiResponse[String]

  def replicationStatus(parameters: PeriodParameters): ApiResponse[ReplicationStatusPage]

  def systemStatus(parameters: PeriodParameters): ApiResponse[SystemStatusPage]

}
