package kpn.server.api.status

import kpn.api.common.status.LogPage
import kpn.api.common.status.PeriodParameters
import kpn.api.common.status.ReplicationStatusPage
import kpn.api.common.status.Status
import kpn.api.common.status.SystemStatusPage
import kpn.api.custom.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StatusController(statusFacade: StatusFacade) {

  @GetMapping(value = Array("/api/status"))
  def status(): ApiResponse[Status] = {
    statusFacade.status()
  }

  @GetMapping(value = Array("/api/status/ok"))
  def ok(): ApiResponse[String] = {
    statusFacade.statusOk()
  }

  @PostMapping(value = Array("/api/status/replication"))
  def replicationStatus(@RequestBody parameters: PeriodParameters): ApiResponse[ReplicationStatusPage] = {
    statusFacade.replicationStatus(parameters)
  }

  @PostMapping(value = Array("/api/status/system"))
  def systemStatus(@RequestBody parameters: PeriodParameters): ApiResponse[SystemStatusPage] = {
    statusFacade.systemStatus(parameters)
  }

  @PostMapping(value = Array("/api/status/log"))
  def logStatus(@RequestBody parameters: PeriodParameters): ApiResponse[LogPage] = {
    statusFacade.logStatus(parameters)
  }

}
