package kpn.server.api.status

import kpn.api.common.status.BarChart
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
