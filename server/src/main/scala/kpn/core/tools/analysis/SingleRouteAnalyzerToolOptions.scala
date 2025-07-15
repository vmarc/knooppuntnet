package kpn.core.tools.analysis

import kpn.database.base.Options

object SingleRouteAnalyzerToolOptions extends Options[SingleRouteAnalyzerToolOptions] {

  def parse(args: Array[String]): Option[SingleRouteAnalyzerToolOptions] = {
    optionParser.parse(args, SingleRouteAnalyzerToolOptions())
  }

  private def optionParser: scopt.OptionParser[SingleRouteAnalyzerToolOptions] = {
    new scopt.OptionParser[SingleRouteAnalyzerToolOptions]("SingleRouteAnalyzerToolOptions") {
      head("AnalyzerStartTool")

      opt[String]('d', "database").required() valueName "<database>" action { (x, c) =>
        c.copy(databaseName = x)
      } text "database name"

      opt[String]('r', "routeId").required() valueName "<database>" action { (x, c) =>
        c.copy(routeId = x.toLong)
      } text "route id"
    }
  }
}

case class SingleRouteAnalyzerToolOptions(
  databaseName: String = "",
  routeId: Long = 0
)
