package kpn.server.api.status

import kpn.api.common.status.ActionTimestamp
import kpn.api.common.status.BarChart
import kpn.api.common.status.BarChart2D
import kpn.api.common.status.BarChart2dValue
import kpn.api.common.status.NameValue
import kpn.api.common.status.PeriodParameters
import kpn.api.common.status.ReplicationStatusPage
import kpn.api.common.status.Status
import kpn.api.common.status.SystemStatusPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.BackendActionsRepository
import org.springframework.stereotype.Component

@Component
class StatusFacadeImpl(
  analysisRepository: AnalysisRepository,
  backendActionsRepository: BackendActionsRepository
) extends StatusFacade {

  override def status(): ApiResponse[Status] = {
    val response = ApiResponse(analysisRepository.lastUpdated(), 1, Some(Status(ActionTimestamp.now())))
    TimestampLocal.localize(response)
    response
  }

  override def replicationStatus(parameters: PeriodParameters): ApiResponse[ReplicationStatusPage] = {

    val replicationDelays = backendActionsRepository.query(parameters, "replication-delay", average = true)
    val updateDelays = backendActionsRepository.query(parameters, "update-delay", average = true)
    val analysisDelays = backendActionsRepository.query(parameters, "analysis-delay", average = true)

    val delay = BarChart2D(
      "",
      "",
      "",
      replicationDelays.indices.map { index =>
        val replicationDelay = replicationDelays(index)
        val updateDelay = updateDelays(index)
        val analysisDelay = analysisDelays(index)
        BarChart2dValue(
          replicationDelay.name,
          Seq(
            NameValue("replication", replicationDelay.value),
            NameValue("update", updateDelay.value - replicationDelay.value),
            NameValue("analysis", analysisDelay.value /*- updateDelay.value*/)
          )
        )
      }
    )

    val analysisDelay = BarChart(analysisDelays)
    val updateDelay = BarChart(updateDelays)
    val replicationDelay = BarChart(replicationDelays)

    val replicationBytes = BarChart(backendActionsRepository.query(parameters, "replication-bytes"))
    val replicationElements = BarChart(backendActionsRepository.query(parameters, "replication-elements"))
    val replicationChangeSets = BarChart(backendActionsRepository.query(parameters, "replication-changesets"))

    val periodTitle = parameters.period match {
      case "year" => parameters.year.toString
      case "month" => f"${parameters.year} ${parameters.month.get}"
      case "week" => f"${parameters.year} ${parameters.week.get}"
      case "day" => f"${parameters.year}-${parameters.month.get}-${parameters.day.get}"
      case "hour" => f"${parameters.year}-${parameters.month.get}-${parameters.day.get} hour: ${parameters.hour.get}"
      case _ => ""
    }

    val previous = parameters.period match {
      case "year" => "previous-yearlink"
      case "month" => "previous-monthlink"
      case "week" => "previous-weeklink"
      case "day" => "previous-daylink"
      case "hour" => "previous-hourlink"
      case _ => ""
    }

    val next = parameters.period match {
      case "year" => "next-yearlink"
      case "month" => "next-monthlink"
      case "week" => "next-weeklink"
      case "day" => "next-daylink"
      case "hour" => "next-hourlink"
      case _ => ""
    }

    ApiResponse(
      None,
      1,
      Some(
        ReplicationStatusPage(
          ActionTimestamp.now(),
          parameters.period,
          periodTitle,
          previous,
          next,
          delay,
          analysisDelay,
          updateDelay,
          replicationDelay,
          replicationBytes,
          replicationElements,
          replicationChangeSets
        )
      )
    )
  }

  override def systemStatus(parameters: PeriodParameters): ApiResponse[SystemStatusPage] = {

    val backendDiskSpaceUsed = BarChart(backendActionsRepository.query(parameters, "backend-disk-space-used", average = true))
    val backendDiskSpaceAvailable = BarChart(backendActionsRepository.query(parameters, "backend-disk-space-available", average = true))
    val backendDiskSpaceOverpass = BarChart(backendActionsRepository.query(parameters, "backend-disk-space-overpass", average = true))

    val analysisDocCount = BarChart(backendActionsRepository.query(parameters, "backend-analysis-docs", average = true))
    val analysisDiskSize = BarChart(backendActionsRepository.query(parameters, "backend-analysis-disk-size", average = true))
    val analysisDiskSizeExternal = BarChart(backendActionsRepository.query(parameters, "backend-analysis-data-size-external", average = true))
    val analysisDataSize = BarChart(backendActionsRepository.query(parameters, "backend-analysis-data-size", average = true))

    val changesDocCount = BarChart(backendActionsRepository.query(parameters, "backend-changes-docs", average = true))
    val changesDiskSize = BarChart(backendActionsRepository.query(parameters, "backend-changes-disk-size", average = true))
    val changesDiskSizeExternal = BarChart(backendActionsRepository.query(parameters, "backend-changes-data-size-external", average = true))
    val changesDataSize = BarChart(backendActionsRepository.query(parameters, "backend-changes-data-size", average = true))

    val periodTitle = parameters.period match {
      case "year" => parameters.year.toString
      case "month" => f"${parameters.year} ${parameters.month.get}"
      case "week" => f"${parameters.year} ${parameters.week.get}"
      case "day" => f"${parameters.year}-${parameters.month.get}-${parameters.day.get}"
      case "hour" => f"${parameters.year}-${parameters.month.get}-${parameters.day.get} hour: ${parameters.hour.get}"
      case _ => ""
    }

    val previous = parameters.period match {
      case "year" => "previous-yearlink"
      case "month" => "previous-monthlink"
      case "week" => "previous-weeklink"
      case "day" => "previous-daylink"
      case "hour" => "previous-hourlink"
      case _ => ""
    }

    val next = parameters.period match {
      case "year" => "next-yearlink"
      case "month" => "next-monthlink"
      case "week" => "next-weeklink"
      case "day" => "next-daylink"
      case "hour" => "next-hourlink"
      case _ => ""
    }

    ApiResponse(
      None,
      1,
      Some(
        SystemStatusPage(
          ActionTimestamp.now(),
          parameters.period,
          periodTitle,
          previous,
          next,
          backendDiskSpaceUsed,
          backendDiskSpaceAvailable,
          backendDiskSpaceOverpass,
          analysisDocCount,
          analysisDiskSize,
          analysisDiskSizeExternal,
          analysisDataSize,
          changesDocCount,
          changesDiskSize,
          changesDiskSizeExternal,
          changesDataSize
        )
      )
    )
  }

}
