package kpn.core.tools.operation

object OperationToolOptions {

  def parse(args: Array[String]): Option[OperationToolOptions] = {
    optionParser.parse(args, OperationToolOptions())
  }

  private def optionParser: scopt.OptionParser[OperationToolOptions] = {

    new scopt.OptionParser[OperationToolOptions]("OperationTool") {
      head("OperationTool")

      opt[String]('w', "web") valueName "<true|false>" action { (x, c) =>
        c.copy(web = x == "true")
      } text "web server"
    }
  }
}

case class OperationToolOptions(
  web: Boolean = false
)
