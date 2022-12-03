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
    }
  }
}

case class MonitorUpdateToolOptions(
  databaseName: String = ""
)

