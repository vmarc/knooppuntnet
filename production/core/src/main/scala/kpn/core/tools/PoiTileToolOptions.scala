package kpn.core.tools

object PoiTileToolOptions {

  def parse(args: Array[String]): Option[PoiTileToolOptions] = {
    optionParser.parse(args, PoiTileToolOptions())
  }

  private def optionParser: scopt.OptionParser[PoiTileToolOptions] = {
    new scopt.OptionParser[PoiTileToolOptions]("PoiTileTool") {
      head("PoiTileTool")

      opt[String]('t', "tileDir") required() valueName "<tile-root-directory>" action { (x, c) =>
        c.copy(tileDir = x)
      } text "tiles root directory name"

      opt[String]('d', "database") required() valueName "<poi-database>" action { (x, c) =>
        c.copy(poiDatabaseName = x)
      } text "poi database name"
    }
  }
}

case class PoiTileToolOptions(
  poiDatabaseName: String = "",
  tileDir: String = ""
)
