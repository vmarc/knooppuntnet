package kpn.core.tools.`export`

object NodeCyclerExportToolOptions {

  def parse(args: Array[String]): Option[NodeCyclerExportToolOptions] = {
    optionParser.parse(args, NodeCyclerExportToolOptions())
  }

  private def optionParser: scopt.OptionParser[NodeCyclerExportToolOptions] = {
    new scopt.OptionParser[NodeCyclerExportToolOptions]("NodeCyclerExportTool") {
      head("NodeCyclerExportTool")

      opt[String]('h', "host").required() valueName "<database-host>" action { (x, c) =>
        c.copy(host = x)
      } text "analysis database host name"

      opt[String]('d', "database").required() valueName "<analysis-database>" action { (x, c) =>
        c.copy(database = x)
      } text "analysis database name"

      opt[String]('e', "exportDir").required() valueName "<export-directory>" action { (x, c) =>
        c.copy(exportDir = x)
      } text "export directory"
    }
  }
}

case class NodeCyclerExportToolOptions(
  host: String = "",
  database: String = "",
  exportDir: String = ""
)
