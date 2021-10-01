package kpn.core.tools.analysis

object AnalysisStartToolOptions {

  def parse(args: Array[String]): Option[AnalysisStartToolOptions] = {
    optionParser.parse(args, AnalysisStartToolOptions())
  }

  private def optionParser: scopt.OptionParser[AnalysisStartToolOptions] = {
    new scopt.OptionParser[AnalysisStartToolOptions]("AnalyzerStartTool") {
      head("AnalyzerStartTool")

      opt[String]('d', "database").required() valueName "<database>" action { (x, c) =>
        c.copy(databaseName = x)
      } text "database name"
    }
  }
}

case class AnalysisStartToolOptions(
  databaseName: String = ""
)
