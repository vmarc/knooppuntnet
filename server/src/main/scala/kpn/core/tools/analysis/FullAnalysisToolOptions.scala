package kpn.core.tools.analysis

object FullAnalysisToolOptions {

  def parse(args: Array[String]): Option[FullAnalysisToolOptions] = {
    optionParser.parse(args, FullAnalysisToolOptions())
  }

  private def optionParser: scopt.OptionParser[FullAnalysisToolOptions] = {
    new scopt.OptionParser[FullAnalysisToolOptions]("FullAnalysisToolOptions") {
      head("AnalyzerStartTool")

      opt[String]('d', "database").required() valueName "<database>" action { (x, c) =>
        c.copy(databaseName = x)
      } text "database name"
    }
  }
}

case class FullAnalysisToolOptions(
  databaseName: String = ""
)
