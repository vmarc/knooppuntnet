package kpn.core.tools.monitor

object MonitorDemoUpdateToolOptions {

  def parse(args: Array[String]): Option[MonitorDemoUpdateToolOptions] = {
    optionParser.parse(args, MonitorDemoUpdateToolOptions())
  }

  private def optionParser: scopt.OptionParser[MonitorDemoUpdateToolOptions] = {

    new scopt.OptionParser[MonitorDemoUpdateToolOptions]("MonitorDemoUpdateTool") {
      head("TileTool")

      opt[String]('d', "database").required() valueName "<database-name>" action { (x, c) =>
        c.copy(databaseName = x)
      } text "database name"
    }
  }
}

case class MonitorDemoUpdateToolOptions(
  databaseName: String = ""
)

