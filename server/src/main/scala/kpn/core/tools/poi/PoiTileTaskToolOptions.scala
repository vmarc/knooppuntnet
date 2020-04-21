package kpn.core.tools.poi

object PoiTileTaskToolOptions {

  def parse(args: Array[String]): Option[PoiTileTaskToolOptions] = {
    optionParser.parse(args, PoiTileTaskToolOptions())
  }

  private def optionParser: scopt.OptionParser[PoiTileTaskToolOptions] = {
    new scopt.OptionParser[PoiTileTaskToolOptions]("PoiTileTaskTool") {
      head("PoiTileTaskTool")

      opt[String]('h', "host").required() valueName "<host>" action { (x, c) =>
        c.copy(host = x)
      } text "host"

      opt[String]('p', "poi-database").required() valueName "<poi-database>" action { (x, c) =>
        c.copy(poiDatabaseName = x)
      } text "poi database name"

      opt[String]('t', "task-database").required() valueName "<task-database>" action { (x, c) =>
        c.copy(taskDatabaseName = x)
      } text "task database name"

    }
  }
}

case class PoiTileTaskToolOptions(
  host: String = "",
  poiDatabaseName: String = "",
  taskDatabaseName: String = ""
)
