package kpn.core.tools.analysis

object ChangeSetInfoToolOptions {

  def parse(args: Array[String]): Option[ChangeSetInfoToolOptions] = {
    optionParser.parse(args, ChangeSetInfoToolOptions())
  }

  private def optionParser: scopt.OptionParser[ChangeSetInfoToolOptions] = {
    new scopt.OptionParser[ChangeSetInfoToolOptions]("ChangeSetInfoTool") {
      head("ChangeSetInfoTool")

      opt[String]('c', "changesets").required() valueName "<change-sets-database>" action { (x, c) =>
        c.copy(changeSetsDatabaseName = x)
      } text "changesets database name"

      opt[String]('t', "tasks").required() valueName "<tasks-database>" action { (x, c) =>
        c.copy(tasksDatabaseName = x)
      } text "tasks database name"

    }
  }
}

case class ChangeSetInfoToolOptions(
  changeSetsDatabaseName: String = "",
  tasksDatabaseName: String = ""
)
