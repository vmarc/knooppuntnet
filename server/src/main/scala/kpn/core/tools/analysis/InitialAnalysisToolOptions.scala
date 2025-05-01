package kpn.core.tools.analysis

object InitialAnalysisToolOptions {

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
