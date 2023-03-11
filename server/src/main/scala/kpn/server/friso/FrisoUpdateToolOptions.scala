package kpn.server.friso

object FrisoUpdateToolOptions {

  def parse(args: Array[String]): Option[FrisoUpdateToolOptions] = {
    optionParser.parse(args, FrisoUpdateToolOptions())
  }

  private def optionParser: scopt.OptionParser[FrisoUpdateToolOptions] = {

    new scopt.OptionParser[FrisoUpdateToolOptions]("FrisoUpdateTool") {
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

case class FrisoUpdateToolOptions(
  databaseName: String = "",
  remote: Boolean = false
)
