package kpn.server.api.status

import kpn.api.common.status.BarChart2D
import kpn.api.common.status.BarChart2dValue
import kpn.api.common.status.NameValue
import kpn.api.custom.ApiResponse
import org.springframework.stereotype.Component

@Component
class StatusFacadeImpl extends StatusFacade {

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
