package kpn.core.tools

object TileToolOptions {
  def parse(args: Array[String]): Option[TileToolOptions] = {
    optionParser.parse(args, TileToolOptions())
  }

  private def optionParser: scopt.OptionParser[TileToolOptions] = {
    new scopt.OptionParser[TileToolOptions]("TileTool") {
      head("TileTool")

      opt[String]('t', "tileDir") required() valueName "<tile-root-directory>" action { (x, c) =>
        c.copy(tileDir = x)
      } text "tile directory"

      opt[String]('a', "analysis") required() valueName "<analysis-database>" action { (x, c) =>
        c.copy(analysisDatabaseName = x)
      } text "analysis database name"
    }
  }
}

case class TileToolOptions(
  analysisDatabaseName: String = "",
  tileDir: String = ""
)
