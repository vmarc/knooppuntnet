package kpn.core.tools.poi

object PoiTileUpdateToolOptions {

  def parse(args: Array[String]): Option[PoiTileUpdateToolOptions] = {
    optionParser.parse(args, PoiTileUpdateToolOptions())
  }

  private def optionParser: scopt.OptionParser[PoiTileUpdateToolOptions] = {
    new scopt.OptionParser[PoiTileUpdateToolOptions]("PoiTileUpdateTool") {
      head("PoiTileUpdateTool")

      opt[String]('d', "tile-dir") required() valueName "<tile-root-directory>" action { (x, c) =>
        c.copy(tileDir = x)
      } text "tiles root directory name"

      opt[String]('h', "host") required() valueName "<host>" action { (x, c) =>
        c.copy(host = x)
      } text "host"

      opt[String]('p', "poi-database") required() valueName "<poi-database>" action { (x, c) =>
        c.copy(poiDatabaseName = x)
      } text "poi database name"

      opt[String]('t', "task-database") required() valueName "<task-database>" action { (x, c) =>
        c.copy(taskDatabaseName = x)
      } text "task database name"

    }
  }
}

case class PoiTileUpdateToolOptions(
  host: String = "",
  poiDatabaseName: String = "",
  taskDatabaseName: String = "",
  tileDir: String = ""
)
