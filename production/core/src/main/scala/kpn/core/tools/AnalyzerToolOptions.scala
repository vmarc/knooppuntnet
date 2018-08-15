package kpn.core.tools

object AnalyzerToolOptions {

  def parse(args: Array[String]): Option[AnalyzerToolOptions] = {
    optionParser.parse(args, AnalyzerToolOptions())
  }

  private def optionParser: scopt.OptionParser[AnalyzerToolOptions] = {
    new scopt.OptionParser[AnalyzerToolOptions]("AnalyzerTool") {
      head("AnalyzerTool")

      opt[String]('c', "changes") required() valueName "<changes-database>" action { (x, c) =>
        c.copy(changeDatabaseName = x)
      } text "changes database name"

      opt[String]('a', "analysis") required() valueName "<analysis-database>" action { (x, c) =>
        c.copy(analysisDatabaseName = x)
      } text "analysis database name"

      opt[String]('t', "tasks") required() valueName "<tasks-database>" action { (x, c) =>
        c.copy(taskDatabaseName = x)
      } text "tasks database name"

      opt[Unit]('i', "init-db") valueName "<initialize-database>" action { (_, c) =>
        c.copy(initialDatabaseLoad = true)
      } text "initial database load"

    }
  }
}

case class AnalyzerToolOptions(
  analysisDatabaseName: String = "",
  changeDatabaseName: String = "",
  taskDatabaseName: String = "",
  initialDatabaseLoad: Boolean = false
)
