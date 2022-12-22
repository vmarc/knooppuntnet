package kpn.core.tools.monitor

object MonitorUpdateToolOptions {

  def parse(args: Array[String]): Option[MonitorUpdateToolOptions] = {
    optionParser.parse(args, MonitorUpdateToolOptions())
  }

  private def optionParser: scopt.OptionParser[MonitorUpdateToolOptions] = {

    new scopt.OptionParser[MonitorUpdateToolOptions]("MonitorUpdateTool") {
      head("TileTool")

      opt[String]('d', "database").required() valueName "<database-name>" action { (x, c) =>
        c.copy(databaseName = x)
      } text "database name"

      opt[String]('r', "remote").required() valueName "<true|false>" action { (x, c) =>
        c.copy(remote = x == "true")
      } text "remote overpass queries"
    }
  }
}

case class MonitorUpdateToolOptions(
  databaseName: String = "",
  remote: Boolean = false
)

