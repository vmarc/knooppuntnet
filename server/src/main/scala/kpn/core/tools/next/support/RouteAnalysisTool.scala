package kpn.core.tools.next.support

import kpn.api.custom.Timestamp
import kpn.core.util.Log

object RouteAnalysisTool {
  def main(args: Array[String]): Unit = {
    new RouteAnalysisTool().analyze()
  }

  val essenOkRouteIds: Seq[Long] = Seq(
    13844575L, // 01-02
    3294187L, // 01-08
    13844576L, // 01-36
    3665081L, // 02-92
    13844574L, // 02-11
  )

  val law9: Seq[Long] = Seq(
    7973533L, // LAW-9 super route containing other super routes
    312993L, // Pieterpad deel 1 - Pieterburen-Vorden
    8831649L,
    8832176L,
    8832222L,
    8832221L,
    8832220L,
    8832392L,
    8832391L,
    8832709L,
    8832708L,
    8832707L,
    8832706L,
    8832705L,
    8832704L,
    156951L, // Pieterpad deel 2 - Vorden-Maastricht Pietersberg
    8834446L,
    8834445L,
    8835026L,
    8835025L,
    8835024L,
    8835656L,
    8835655L,
    8835654L,
    8835653L,
    8835652L,
    8835651L,
    8835650L,
    8835649L,
  )
}

class RouteAnalysisTool() {

  private val timestamp = Timestamp(2025, 1, 1)
  private val log = Log(classOf[RouteAnalysisTool])

  def analyze(): Unit = {
    // TODO redesign - populate temp collections from rawRepository, and run AnalysisStartTool
  }
}
