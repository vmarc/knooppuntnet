package kpn.core.tools.tile

object TileToolOptions {

  def parse(args: Array[String]): Option[TileToolOptions] = {
    optionParser.parse(args, TileToolOptions())
  }

  private def optionParser: scopt.OptionParser[TileToolOptions] = {

    new scopt.OptionParser[TileToolOptions]("TileTool") {
      head("TileTool")

      opt[String]('h', "host").required() valueName "<database-host>" action { (x, c) =>
        c.copy(databaseHost = x)
      } text "database host name"

      opt[String]('t', "tileDir").required() valueName "<tile-root-directory>" action { (x, c) =>
        c.copy(tileDir = x)
      } text "tile directory"

      opt[String]('d', "database").required() valueName "<database-name>" action { (x, c) =>
        c.copy(databaseName = x)
      } text "database name"
    }
  }
}

case class TileToolOptions(
  databaseHost: String = "",
  databaseName: String = "",
  tileDir: String = ""
)
