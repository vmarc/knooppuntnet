package kpn.core.replicate

object ReplicatorToolOptions {

  def parse(args: Array[String]): Option[ReplicatorToolOptions] = {
    optionParser.parse(args, ReplicatorToolOptions())
  }

  private def optionParser: scopt.OptionParser[ReplicatorToolOptions] = {
    new scopt.OptionParser[ReplicatorToolOptions]("ReplicatorTool") {
      head("ReplicatorTool")

      opt[String]('a', "actions-database") required() valueName "<database-name>" action { (x, c) =>
        c.copy(actionsDatabaseName = x)
      } text "actions database name"
    }
  }
}

case class ReplicatorToolOptions(
  actionsDatabaseName: String = ""
)
