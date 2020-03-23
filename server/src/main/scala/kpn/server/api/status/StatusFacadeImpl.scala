package kpn.server.api.status

import kpn.api.common.status.BarChart
import kpn.api.common.status.BarChart2D
import kpn.api.common.status.BarChart2dValue
import kpn.api.common.status.NameValue
import kpn.api.custom.ApiResponse
import kpn.core.common.Time
import kpn.server.repository.BackendActionsRepository
import org.springframework.stereotype.Component

@Component
class StatusFacadeImpl(
  backendActionsRepository: BackendActionsRepository
) extends StatusFacade {

  override def dayAction(action: String): ApiResponse[BarChart] = {
    val nameValues = backendActionsRepository.dayAction(Time.now, action)
    ApiResponse(None, 1, Some(BarChart(toDayValues(nameValues))))
  }

  override def dayActionAverage(action: String): ApiResponse[BarChart] = {
    val nameValues = backendActionsRepository.dayActionAverage(Time.now, action)
    ApiResponse(None, 1, Some(BarChart(toDayValues(nameValues))))
  }

  override def dayDelay(): ApiResponse[BarChart2D] = {

    val replicationDelays = delayMap("replication-delay")
    val updateDelays = delayMap("update-delay")
    val analysisDelays = delayMap("analysis-delay")

    val data = (0 to 23).map { hourIndex =>
      val hour = f"${hourIndex}%02d"
      val replicationDelay = delayAt(replicationDelays, hour)
      val updateDelay = delayAt(updateDelays, hour)
      val analysisDelay = delayAt(analysisDelays, hour)

      BarChart2dValue(
        hour,
        Seq(
          NameValue("replication", replicationDelay),
          NameValue("update", updateDelay - replicationDelay),
          NameValue("analysis", analysisDelay /* - updateDelay */)
        )
      )
    }

    ApiResponse(None, 1, Some(BarChart2D("", "", "", data)))
  }

  private def delayMap(action: String): Map[String, NameValue] = {
    val delays = backendActionsRepository.dayActionAverage(Time.now, action)
    delays.map(nv => nv.name -> nv).toMap
  }

  private def delayAt(delays: Map[String, NameValue], hour: String): Long = {
    delays.get(hour).map(_.value).getOrElse(0)
  }


  private def toDayValues(nameValues: Seq[NameValue]): Seq[NameValue] = {
    val nameValueMap = nameValues.map(nv => nv.name -> nv).toMap
    (0 to 23).map { hour =>
      val hourString = f"${hour}%02d"
      nameValueMap.get(hourString) match {
        case Some(nameValue) => nameValue
        case None => NameValue(hourString, 0)
      }
    }
  }

  override def example(): ApiResponse[BarChart2D] = {
    ApiResponse(
      None,
      1,
      Some(
        BarChart2D(
          "Week 22",
          "Changesets",
          "",
          Seq(
            BarChart2dValue("Monday", Seq(NameValue("Impact", 20), NameValue("No impact", 100))),
            BarChart2dValue("Tuesday", Seq(NameValue("Impact", 45), NameValue("No impact", 80))),
            BarChart2dValue("Wednesday", Seq(NameValue("Impact", 60), NameValue("No impact", 70))),
            BarChart2dValue("Thursday", Seq(NameValue("Impact", 10), NameValue("No impact", 30))),
            BarChart2dValue("Friday", Seq(NameValue("Impact", 70), NameValue("No impact", 75))),
            BarChart2dValue("Saturday", Seq(NameValue("Impact", 20), NameValue("No impact", 50))),
            BarChart2dValue("Sunday", Seq(NameValue("Impact", 30), NameValue("No impact", 40)))
          )
        )
      )
    )
  }
}
