package kpn.core.tools.analysis

import kpn.database.base.Options

object InitialAnalysisToolOptions extends Options[InitialAnalysisToolOptions] {

  def parse(args: Array[String]): Option[InitialAnalysisToolOptions] = {
    optionParser.parse(args, InitialAnalysisToolOptions())
  }

  private def optionParser: scopt.OptionParser[InitialAnalysisToolOptions] = {
    new scopt.OptionParser[InitialAnalysisToolOptions]("InitialAnalysisToolOptions") {
      head("AnalyzerStartTool")

      opt[String]('d', "database").required() valueName "<database>" action { (x, c) =>
        c.copy(databaseName = x)
      } text "database name"
    }
  }
}

case class InitialAnalysisToolOptions(
  databaseName: String = ""
)
