package kpn.server.api.status

import kpn.api.common.status.BarChart
import kpn.api.common.status.BarChart2D
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

  @GetMapping(value = Array("/json-api/status"))
  def status(): ApiResponse[Status] = {
    statusFacade.status()
  }

  @PostMapping(value = Array("/json-api/status/replication"))
  def replicationStatus(@RequestBody parameters: PeriodParameters): ApiResponse[ReplicationStatusPage] = {
    statusFacade.replicationStatus(parameters)
  }

  @PostMapping(value = Array("/json-api/status/system"))
  def systemStatus(@RequestBody parameters: PeriodParameters): ApiResponse[SystemStatusPage] = {
    statusFacade.systemStatus(parameters)
  }

  @GetMapping(value = Array("/json-api/status/example"))
  def example(): ApiResponse[BarChart2D] = {
    statusFacade.example()
  }

  @GetMapping(value = Array("/json-api/status/day-delay"))
  def dayDelay(): ApiResponse[BarChart2D] = {
    statusFacade.dayDelay()
  }

  @GetMapping(value = Array("/json-api/status/day-replication-delay"))
  def dayReplicationDelay(): ApiResponse[BarChart] = {
    statusFacade.dayActionAverage("replication-delay")
  }

  @GetMapping(value = Array("/json-api/status/day-replication-bytes"))
  def dayReplicationFileSize(): ApiResponse[BarChart] = {
    statusFacade.dayAction("replication-bytes")
  }

  @GetMapping(value = Array("/json-api/status/day-replication-elements"))
  def dayReplicationElementCount(): ApiResponse[BarChart] = {
    statusFacade.dayAction("replication-elements")
  }

  @GetMapping(value = Array("/json-api/status/day-replication-changesets"))
  def dayReplicationChangeSetCount(): ApiResponse[BarChart] = {
    statusFacade.dayAction("replication-changesets")
  }

  @GetMapping(value = Array("/json-api/status/day-update-delay"))
  def dayUpdateDelay(): ApiResponse[BarChart] = {
    statusFacade.dayActionAverage("update-delay")
  }

  @GetMapping(value = Array("/json-api/status/day-analysis-delay"))
  def dayAnalysisDelay(): ApiResponse[BarChart] = {
    statusFacade.dayActionAverage("analysis-delay")
  }

}
