package kpn.core.tools.db

object DatabaseCompactionToolOptions {

  def parse(args: Array[String]): Option[DatabaseCompactionToolOptions] = {
    optionParser.parse(args, DatabaseCompactionToolOptions())
  }

  private def optionParser: scopt.OptionParser[DatabaseCompactionToolOptions] = {
    new scopt.OptionParser[DatabaseCompactionToolOptions]("DatabaseCompactionTool") {
      head("DatabaseCompactionTool")

      opt[String]('h', "host").required() valueName "<hostname>" action { (x, c) =>
        c.copy(host = x)
      } text "database host name"

      opt[String]('c', "compactions").required() valueName "<filename>" action { (x, c) =>
        c.copy(compactions = x)
      } text "compactions configuration json file"
    }
  }
}

case class DatabaseCompactionToolOptions(
  host: String = "",
  compactions: String = ""
)
