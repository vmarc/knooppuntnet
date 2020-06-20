package kpn.core.tools.tile

object TileToolOptions {
  def parse(args: Array[String]): Option[TileToolOptions] = {
    optionParser.parse(args, TileToolOptions())
  }

  private def optionParser: scopt.OptionParser[TileToolOptions] = {
    new scopt.OptionParser[TileToolOptions]("TileTool") {
      head("TileTool")

      opt[String]('h', "host").required() valueName "<database-host>" action { (x, c) =>
        c.copy(analysisDatabaseHost = x)
      } text "analysis database host name"

      opt[String]('t', "tileDir").required() valueName "<tile-root-directory>" action { (x, c) =>
        c.copy(tileDir = x)
      } text "tile directory"

      opt[String]('a', "analysis").required() valueName "<analysis-database>" action { (x, c) =>
        c.copy(analysisDatabaseName = x)
      } text "analysis database name"
    }
  }
}

case class TileToolOptions(
  analysisDatabaseHost: String = "",
  analysisDatabaseName: String = "",
  tileDir: String = ""
)
